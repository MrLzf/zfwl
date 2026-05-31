package cn.iocoder.yudao.module.tutor.service.teacher;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.teacher.TutorTeacherProfileMapper;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;
import cn.iocoder.yudao.module.tutor.service.point.TutorPointRewardService;
import cn.iocoder.yudao.module.tutor.service.profile.TutorUserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

class TutorTeacherProfileServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorTeacherProfileServiceImpl teacherProfileService;

    @Mock
    private TutorTeacherProfileMapper teacherProfileMapper;
    @Mock
    private TutorUserProfileService userProfileService;
    @Mock
    private TutorPointRewardService pointRewardService;

    @Test
    void getOrCreateTeacherProfile_whenMissing_createsDraftProfile() {
        Long userId = 100L;
        when(userProfileService.getProfile(userId)).thenReturn(TutorUserProfileDO.builder()
                .id(20L).userId(userId).role(TutorUserRoleEnum.TEACHER.getRole()).build());

        teacherProfileService.getOrCreateTeacherProfile(userId);

        ArgumentCaptor<TutorTeacherProfileDO> captor = ArgumentCaptor.forClass(TutorTeacherProfileDO.class);
        verify(teacherProfileMapper).insert(captor.capture());
        TutorTeacherProfileDO teacherProfile = captor.getValue();
        assertEquals(userId, teacherProfile.getUserId());
        assertEquals(20L, teacherProfile.getProfileId());
        assertEquals(TutorAuditStatusEnum.DRAFT.getStatus(), teacherProfile.getCertificationStatus());
    }

    @Test
    void saveTeacherProfile_whenNotRewarded_rewardsRoleProfileCompletion() {
        Long userId = 100L;
        when(userProfileService.getProfile(userId)).thenReturn(TutorUserProfileDO.builder()
                .id(20L).userId(userId).role(TutorUserRoleEnum.TEACHER.getRole()).build());
        when(teacherProfileMapper.selectByUserId(userId)).thenReturn(TutorTeacherProfileDO.builder().id(30L).build());

        teacherProfileService.saveTeacherProfile(userId, new cn.iocoder.yudao.module.tutor.controller.app.teacher.vo.AppTutorTeacherProfileSaveReqVO());

        verify(pointRewardService).reward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE,
                "role_profile_complete", "首次完善资料");
    }

    @Test
    void saveTeacherProfile_whenAlreadyRewarded_doesNotRewardAgain() {
        Long userId = 100L;
        when(userProfileService.getProfile(userId)).thenReturn(TutorUserProfileDO.builder()
                .id(20L).userId(userId).role(TutorUserRoleEnum.TEACHER.getRole()).build());
        when(teacherProfileMapper.selectByUserId(userId)).thenReturn(TutorTeacherProfileDO.builder().id(30L).build());
        when(pointRewardService.hasReward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE)).thenReturn(true);

        teacherProfileService.saveTeacherProfile(userId, new cn.iocoder.yudao.module.tutor.controller.app.teacher.vo.AppTutorTeacherProfileSaveReqVO());

        verify(pointRewardService, never()).reward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE,
                "role_profile_complete", "首次完善资料");
    }

}
