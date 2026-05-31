package cn.iocoder.yudao.module.tutor.service.parent;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.tutor.controller.app.parent.vo.AppTutorParentProfileSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.parent.TutorParentProfileDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.parent.TutorParentProfileMapper;
import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import cn.iocoder.yudao.module.tutor.service.point.TutorPointRewardService;
import cn.iocoder.yudao.module.tutor.service.profile.TutorUserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

class TutorParentProfileServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorParentProfileServiceImpl parentProfileService;
    @Mock
    private TutorParentProfileMapper parentProfileMapper;
    @Mock
    private TutorUserProfileService userProfileService;
    @Mock
    private TutorPointRewardService pointRewardService;

    @Test
    void saveParentProfile_whenFirstSave_rewardsRoleProfileCompletion() {
        Long userId = 100L;
        when(userProfileService.getProfile(userId)).thenReturn(TutorUserProfileDO.builder()
                .id(20L).role(TutorUserRoleEnum.PARENT.getRole()).build());

        parentProfileService.saveParentProfile(userId, new AppTutorParentProfileSaveReqVO());

        verify(pointRewardService).reward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE,
                "role_profile_complete", "首次完善资料");
    }

    @Test
    void saveParentProfile_whenUpdatingLegacyProfile_rewardsRoleProfileCompletion() {
        Long userId = 100L;
        when(userProfileService.getProfile(userId)).thenReturn(TutorUserProfileDO.builder()
                .id(20L).role(TutorUserRoleEnum.PARENT.getRole()).build());
        when(parentProfileMapper.selectByUserId(userId)).thenReturn(TutorParentProfileDO.builder().id(30L).build());

        parentProfileService.saveParentProfile(userId, new AppTutorParentProfileSaveReqVO());

        verify(pointRewardService).reward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE,
                "role_profile_complete", "首次完善资料");
    }

    @Test
    void saveParentProfile_whenUpdatingRewardedProfile_doesNotRewardAgain() {
        Long userId = 100L;
        when(userProfileService.getProfile(userId)).thenReturn(TutorUserProfileDO.builder()
                .id(20L).role(TutorUserRoleEnum.PARENT.getRole()).build());
        when(parentProfileMapper.selectByUserId(userId)).thenReturn(TutorParentProfileDO.builder().id(30L).build());
        when(pointRewardService.hasReward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE)).thenReturn(true);

        parentProfileService.saveParentProfile(userId, new AppTutorParentProfileSaveReqVO());

        verify(pointRewardService, never()).reward(anyLong(), any(), anyString(), anyString());
    }

}
