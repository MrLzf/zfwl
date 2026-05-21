package cn.iocoder.yudao.module.tutor.service.match;

import cn.iocoder.yudao.module.tutor.dal.dataobject.match.TutorMatchRecordDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.match.TutorMatchRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.enums.match.TutorMatchStatusEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.MATCH_CONFIRM_FORBIDDEN;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.MATCH_NOT_EXISTS;

@Service
@Validated
public class TutorMatchServiceImpl implements TutorMatchService {

    @Resource
    private TutorMatchRecordMapper matchRecordMapper;
    @Resource
    private TutorDemandMapper demandMapper;
    @Resource
    private TutorTeacherResumeMapper resumeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TutorMatchRecordDO confirmMatch(Long userId, Long id) {
        TutorMatchRecordDO match = matchRecordMapper.selectById(id);
        if (match == null) {
            throw exception(MATCH_NOT_EXISTS);
        }
        boolean parent = Objects.equals(userId, match.getParentUserId());
        boolean teacher = Objects.equals(userId, match.getTeacherUserId());
        if (!parent && !teacher) {
            throw exception(MATCH_CONFIRM_FORBIDDEN);
        }
        Integer status = calculateStatus(match, parent, teacher);
        LocalDateTime now = LocalDateTime.now();
        LambdaUpdateWrapper<TutorMatchRecordDO> updateWrapper = new LambdaUpdateWrapper<TutorMatchRecordDO>()
                .eq(TutorMatchRecordDO::getId, id)
                .set(TutorMatchRecordDO::getStatus, status);
        if (parent && match.getParentConfirmTime() == null) {
            updateWrapper.set(TutorMatchRecordDO::getParentConfirmTime, now);
        }
        if (teacher && match.getTeacherConfirmTime() == null) {
            updateWrapper.set(TutorMatchRecordDO::getTeacherConfirmTime, now);
        }
        if (TutorMatchStatusEnum.BOTH_CONFIRMED.getStatus().equals(status) && match.getMatchedTime() == null) {
            updateWrapper.set(TutorMatchRecordDO::getMatchedTime, now);
            incrementPublishMatchCount(match);
        }
        matchRecordMapper.update(null, updateWrapper);
        return matchRecordMapper.selectById(id);
    }

    @Override
    public List<TutorMatchRecordDO> getMyMatchList(Long userId) {
        return matchRecordMapper.selectListByUserId(userId);
    }

    private Integer calculateStatus(TutorMatchRecordDO match, boolean parent, boolean teacher) {
        boolean parentConfirmed = match.getParentConfirmTime() != null || parent;
        boolean teacherConfirmed = match.getTeacherConfirmTime() != null || teacher;
        if (parentConfirmed && teacherConfirmed) {
            return TutorMatchStatusEnum.BOTH_CONFIRMED.getStatus();
        }
        if (parentConfirmed) {
            return TutorMatchStatusEnum.PARENT_CONFIRMED.getStatus();
        }
        if (teacherConfirmed) {
            return TutorMatchStatusEnum.TEACHER_CONFIRMED.getStatus();
        }
        return TutorMatchStatusEnum.CONTACT_VIEWED.getStatus();
    }

    private void incrementPublishMatchCount(TutorMatchRecordDO match) {
        if (match.getDemandId() != null) {
            demandMapper.updateMatchCountIncr(match.getDemandId());
        }
        if (match.getResumeId() != null) {
            resumeMapper.updateMatchCountIncr(match.getResumeId());
        }
    }

}
