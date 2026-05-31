package cn.iocoder.yudao.module.tutor.service.point;

import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.tutor.dal.dataobject.point.TutorPointRewardRecordDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.point.TutorPointRewardRecordMapper;
import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Service
@Validated
public class TutorPointRewardServiceImpl implements TutorPointRewardService {

    @Resource
    private TutorPointRewardRecordMapper rewardRecordMapper;
    @Resource
    private MemberPointApi memberPointApi;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private TutorNotifyService tutorNotifyService;

    @Override
    public boolean reward(Long userId, TutorPointTaskTypeEnum taskType, String bizId, String remark) {
        if (rewardRecordMapper.selectByUniqueKey(userId, taskType.getType(), bizId) != null) {
            return false;
        }
        try {
            rewardRecordMapper.insert(TutorPointRewardRecordDO.builder()
                    .userId(userId)
                    .taskType(taskType.getType())
                    .bizId(bizId)
                    .point(taskType.getPoint())
                    .remark(remark)
                    .build());
        } catch (DuplicateKeyException ex) {
            return false;
        }
        memberPointApi.addPoint(userId, taskType.getPoint(), getMemberPointBizType(taskType).getType(), bizId);
        MemberUserRespDTO user = memberUserApi.getUser(userId);
        tutorNotifyService.sendPointChanged(userId, remark, taskType.getPoint(), user == null ? null : user.getPoint(),
                "point", "point_records", bizId, null, null);
        return true;
    }

    @Override
    public boolean hasReward(Long userId, TutorPointTaskTypeEnum taskType) {
        return rewardRecordMapper.existsByTaskType(userId, taskType.getType());
    }

    private MemberPointBizTypeEnum getMemberPointBizType(TutorPointTaskTypeEnum taskType) {
        switch (taskType) {
            case PROFILE_INIT:
                return MemberPointBizTypeEnum.TUTOR_PROFILE_INIT;
            case ROLE_PROFILE_COMPLETE:
                return MemberPointBizTypeEnum.TUTOR_ROLE_PROFILE_COMPLETE;
            case FIVE_STAR_REVIEW:
                return MemberPointBizTypeEnum.TUTOR_FIVE_STAR_REVIEW;
            default:
                throw new IllegalArgumentException("Unsupported tutor point task type: " + taskType);
        }
    }

}
