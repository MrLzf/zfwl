package cn.iocoder.yudao.module.tutor.service.teacher;

import cn.iocoder.yudao.module.tutor.controller.app.teacher.vo.AppTutorTeacherProfileSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.teacher.TutorTeacherProfileMapper;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;
import cn.iocoder.yudao.module.tutor.service.point.TutorPointRewardService;
import cn.iocoder.yudao.module.tutor.service.profile.TutorUserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.PROFILE_NOT_EXISTS;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.PROFILE_ROLE_NOT_TEACHER;

@Service
@Validated
public class TutorTeacherProfileServiceImpl implements TutorTeacherProfileService {

    @Resource
    private TutorTeacherProfileMapper teacherProfileMapper;
    @Resource
    private TutorUserProfileService userProfileService;
    @Resource
    private TutorPointRewardService pointRewardService;

    @Override
    public TutorTeacherProfileDO getTeacherProfile(Long userId) {
        return teacherProfileMapper.selectByUserId(userId);
    }

    @Override
    public TutorTeacherProfileDO getOrCreateTeacherProfile(Long userId) {
        TutorTeacherProfileDO teacherProfile = teacherProfileMapper.selectByUserId(userId);
        if (teacherProfile != null) {
            return teacherProfile;
        }
        TutorUserProfileDO userProfile = validateTeacherRole(userId);
        teacherProfile = TutorTeacherProfileDO.builder()
                .userId(userId)
                .profileId(userProfile.getId())
                .educationLevel("")
                .hasTeacherCertificate(false)
                .subjects("")
                .teachModes("")
                .hourlyPriceMin(0)
                .hourlyPriceMax(0)
                .serviceRadiusKm(0)
                .freeTrialEnabled(false)
                .freeTrialMinutes(0)
                .teachingYears(0)
                .intro("")
                .certificationStatus(TutorAuditStatusEnum.DRAFT.getStatus())
                .ratingAvg(BigDecimal.ZERO)
                .reviewCount(0)
                .build();
        teacherProfileMapper.insert(teacherProfile);
        return teacherProfile;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TutorTeacherProfileDO saveTeacherProfile(Long userId, AppTutorTeacherProfileSaveReqVO reqVO) {
        TutorUserProfileDO userProfile = validateTeacherRole(userId);
        TutorTeacherProfileDO teacherProfile = teacherProfileMapper.selectByUserId(userId);
        if (teacherProfile == null) {
            teacherProfile = TutorTeacherProfileDO.builder()
                    .userId(userId)
                    .profileId(userProfile.getId())
                    .educationLevel(reqVO.getEducationLevel())
                    .schoolName(reqVO.getSchoolName())
                    .major(reqVO.getMajor())
                    .hasTeacherCertificate(reqVO.getHasTeacherCertificate())
                    .subjects(reqVO.getSubjects())
                    .teachModes(reqVO.getTeachModes())
                    .hourlyPriceMin(reqVO.getHourlyPriceMin())
                    .hourlyPriceMax(reqVO.getHourlyPriceMax())
                    .serviceRadiusKm(reqVO.getServiceRadiusKm())
                    .freeTrialEnabled(reqVO.getFreeTrialEnabled())
                    .freeTrialMinutes(Boolean.TRUE.equals(reqVO.getFreeTrialEnabled()) ? reqVO.getFreeTrialMinutes() : 0)
                    .teachingYears(reqVO.getTeachingYears())
                    .intro(reqVO.getIntro())
                    .certificationStatus(TutorAuditStatusEnum.DRAFT.getStatus())
                    .ratingAvg(BigDecimal.ZERO)
                    .reviewCount(0)
                    .build();
            teacherProfileMapper.insert(teacherProfile);
            rewardRoleProfileCompletionIfNeeded(userId);
            return teacherProfile;
        }

        TutorTeacherProfileDO updateObj = TutorTeacherProfileDO.builder()
                .id(teacherProfile.getId())
                .profileId(userProfile.getId())
                .educationLevel(reqVO.getEducationLevel())
                .schoolName(reqVO.getSchoolName())
                .major(reqVO.getMajor())
                .hasTeacherCertificate(reqVO.getHasTeacherCertificate())
                .subjects(reqVO.getSubjects())
                .teachModes(reqVO.getTeachModes())
                .hourlyPriceMin(reqVO.getHourlyPriceMin())
                .hourlyPriceMax(reqVO.getHourlyPriceMax())
                .serviceRadiusKm(reqVO.getServiceRadiusKm())
                .freeTrialEnabled(reqVO.getFreeTrialEnabled())
                .freeTrialMinutes(Boolean.TRUE.equals(reqVO.getFreeTrialEnabled()) ? reqVO.getFreeTrialMinutes() : 0)
                .teachingYears(reqVO.getTeachingYears())
                .intro(reqVO.getIntro())
                .build();
        teacherProfileMapper.updateById(updateObj);
        rewardRoleProfileCompletionIfNeeded(userId);
        return teacherProfileMapper.selectById(teacherProfile.getId());
    }

    private void rewardRoleProfileCompletionIfNeeded(Long userId) {
        if (!pointRewardService.hasReward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE)) {
            pointRewardService.reward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE,
                    "role_profile_complete", "首次完善资料");
        }
    }

    @Override
    public void updateCertificationStatus(Long userId, Integer certificationStatus) {
        TutorTeacherProfileDO teacherProfile = teacherProfileMapper.selectByUserId(userId);
        if (teacherProfile == null) {
            return;
        }
        teacherProfileMapper.updateById(TutorTeacherProfileDO.builder()
                .id(teacherProfile.getId())
                .certificationStatus(certificationStatus)
                .build());
    }

    private TutorUserProfileDO validateTeacherRole(Long userId) {
        TutorUserProfileDO userProfile = userProfileService.getProfile(userId);
        if (userProfile == null) {
            throw exception(PROFILE_NOT_EXISTS);
        }
        if (!Objects.equals(TutorUserRoleEnum.TEACHER.getRole(), userProfile.getRole())) {
            throw exception(PROFILE_ROLE_NOT_TEACHER);
        }
        return userProfile;
    }

}
