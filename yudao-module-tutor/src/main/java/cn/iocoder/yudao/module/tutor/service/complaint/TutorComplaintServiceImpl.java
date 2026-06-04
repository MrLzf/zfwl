package cn.iocoder.yudao.module.tutor.service.complaint;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.complaint.vo.AdminTutorComplaintHandleReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.complaint.vo.AdminTutorComplaintPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.complaint.vo.AppTutorComplaintCreateReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.complaint.TutorComplaintDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.complaint.TutorComplaintMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.enums.target.TutorTargetTypeEnum;
import cn.iocoder.yudao.module.tutor.service.security.TutorContentSecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.*;

@Service
public class TutorComplaintServiceImpl implements TutorComplaintService {

    @Resource
    private TutorComplaintMapper complaintMapper;
    @Resource
    private TutorDemandMapper demandMapper;
    @Resource
    private TutorTeacherResumeMapper resumeMapper;
    @Resource
    private TutorContentSecurityService contentSecurityService;

    @Override
    public TutorComplaintDO createComplaint(Long userId, AppTutorComplaintCreateReqVO reqVO) {
        contentSecurityService.validateTexts("complaint", Arrays.asList(reqVO.getReasonType(), reqVO.getContent(), reqVO.getImageUrls()));
        Long targetUserId = getTargetUserId(reqVO.getTargetType(), reqVO.getTargetId());
        TutorComplaintDO complaint = TutorComplaintDO.builder()
                .userId(userId).targetType(reqVO.getTargetType()).targetId(reqVO.getTargetId())
                .targetUserId(targetUserId).reasonType(reqVO.getReasonType()).content(reqVO.getContent())
                .imageUrls(reqVO.getImageUrls()).status(0).build();
        complaintMapper.insert(complaint);
        return complaint;
    }

    @Override
    public List<TutorComplaintDO> getMyComplaintList(Long userId) {
        return complaintMapper.selectListByUserId(userId);
    }

    @Override
    public PageResult<TutorComplaintDO> getComplaintPage(AdminTutorComplaintPageReqVO reqVO) {
        return complaintMapper.selectPage(reqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TutorComplaintDO handleComplaint(Long handlerId, AdminTutorComplaintHandleReqVO reqVO) {
        TutorComplaintDO complaint = complaintMapper.selectById(reqVO.getId());
        if (complaint == null) {
            throw exception(COMPLAINT_NOT_EXISTS);
        }
        if (StrUtil.isBlank(reqVO.getHandleResult())) {
            throw exception(COMPLAINT_HANDLE_RESULT_REQUIRED);
        }
        complaintMapper.updateById(TutorComplaintDO.builder().id(reqVO.getId()).status(reqVO.getStatus())
                .handlerId(handlerId).handleResult(reqVO.getHandleResult()).handleTime(LocalDateTime.now()).build());
        return complaintMapper.selectById(reqVO.getId());
    }

    private Long getTargetUserId(String targetType, Long targetId) {
        if (TutorTargetTypeEnum.isDemand(targetType)) {
            TutorDemandDO demand = demandMapper.selectById(targetId);
            if (demand == null) {
                throw exception(COMPLAINT_TARGET_NOT_EXISTS);
            }
            return demand.getUserId();
        }
        if (TutorTargetTypeEnum.isResume(targetType)) {
            TutorTeacherResumeDO resume = resumeMapper.selectById(targetId);
            if (resume == null) {
                throw exception(COMPLAINT_TARGET_NOT_EXISTS);
            }
            return resume.getUserId();
        }
        throw exception(COMPLAINT_TARGET_NOT_EXISTS);
    }
}
