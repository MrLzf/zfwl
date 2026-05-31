package cn.iocoder.yudao.module.tutor.service.parent;

import cn.iocoder.yudao.module.tutor.controller.app.parent.vo.AppTutorParentProfileSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.parent.TutorParentProfileDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.parent.TutorParentProfileMapper;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;
import cn.iocoder.yudao.module.tutor.service.point.TutorPointRewardService;
import cn.iocoder.yudao.module.tutor.service.profile.TutorUserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.PROFILE_NOT_EXISTS;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.PROFILE_ROLE_NOT_PARENT;

/**
 * 家长资料 Service 实现。
 */
@Service
@Validated
public class TutorParentProfileServiceImpl implements TutorParentProfileService {

    @Resource
    private TutorParentProfileMapper parentProfileMapper;
    @Resource
    private TutorUserProfileService userProfileService;
    @Resource
    private TutorPointRewardService pointRewardService;

    @Override
    public TutorParentProfileDO getParentProfile(Long userId) {
        return parentProfileMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TutorParentProfileDO saveParentProfile(Long userId, AppTutorParentProfileSaveReqVO reqVO) {
        TutorUserProfileDO userProfile = validateParentRole(userId);
        TutorParentProfileDO parentProfile = parentProfileMapper.selectByUserId(userId);
        if (parentProfile == null) {
            parentProfile = TutorParentProfileDO.builder()
                    .userId(userId)
                    .profileId(userProfile.getId())
                    .childGrade(reqVO.getChildGrade())
                    .subjects(reqVO.getSubjects())
                    .budgetMin(reqVO.getBudgetMin())
                    .budgetMax(reqVO.getBudgetMax())
                    .teachMode(reqVO.getTeachMode())
                    .remark(reqVO.getRemark())
                    .build();
            parentProfileMapper.insert(parentProfile);
            rewardRoleProfileCompletionIfNeeded(userId);
            return parentProfile;
        }

        TutorParentProfileDO updateObj = TutorParentProfileDO.builder()
                .id(parentProfile.getId())
                .profileId(userProfile.getId())
                .childGrade(reqVO.getChildGrade())
                .subjects(reqVO.getSubjects())
                .budgetMin(reqVO.getBudgetMin())
                .budgetMax(reqVO.getBudgetMax())
                .teachMode(reqVO.getTeachMode())
                .remark(reqVO.getRemark())
                .build();
        parentProfileMapper.updateById(updateObj);
        rewardRoleProfileCompletionIfNeeded(userId);
        return parentProfileMapper.selectById(parentProfile.getId());
    }

    private void rewardRoleProfileCompletionIfNeeded(Long userId) {
        if (!pointRewardService.hasReward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE)) {
            pointRewardService.reward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE,
                    "role_profile_complete", "首次完善资料");
        }
    }

    private TutorUserProfileDO validateParentRole(Long userId) {
        TutorUserProfileDO userProfile = userProfileService.getProfile(userId);
        if (userProfile == null) {
            throw exception(PROFILE_NOT_EXISTS);
        }
        if (!Objects.equals(TutorUserRoleEnum.PARENT.getRole(), userProfile.getRole())) {
            throw exception(PROFILE_ROLE_NOT_PARENT);
        }
        return userProfile;
    }

}
