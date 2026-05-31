package cn.iocoder.yudao.module.tutor.service.profile;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileInitReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.profile.TutorUserProfileMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.teacher.TutorTeacherProfileMapper;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;
import cn.iocoder.yudao.module.tutor.service.city.TutorCityService;
import cn.iocoder.yudao.module.tutor.service.point.TutorPointRewardService;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TutorUserProfileServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorUserProfileServiceImpl profileService;

    @Mock
    private TutorUserProfileMapper profileMapper;
    @Mock
    private TutorTeacherProfileMapper teacherProfileMapper;
    @Mock
    private TutorCityService cityService;
    @Mock
    private MemberUserApi memberUserApi;
    @Mock
    private TutorPointRewardService pointRewardService;

    @Test
    void initProfile_whenTeacher_createsDraftTeacherProfile() {
        Long userId = 100L;
        AppTutorProfileInitReqVO reqVO = new AppTutorProfileInitReqVO();
        reqVO.setRole(TutorUserRoleEnum.TEACHER.getRole());
        reqVO.setCityCode("110100");
        when(cityService.validateCityOpened("110100")).thenReturn(TutorCityDO.builder()
                .id(1L).code("110100").name("北京").build());

        profileService.initProfile(userId, reqVO);

        ArgumentCaptor<TutorTeacherProfileDO> captor = ArgumentCaptor.forClass(TutorTeacherProfileDO.class);
        verify(teacherProfileMapper).insert(captor.capture());
        TutorTeacherProfileDO teacherProfile = captor.getValue();
        assertEquals(userId, teacherProfile.getUserId());
        assertEquals("", teacherProfile.getEducationLevel());
        assertEquals("", teacherProfile.getSubjects());
        assertEquals("", teacherProfile.getTeachModes());
        assertEquals("", teacherProfile.getIntro());
        assertEquals(TutorAuditStatusEnum.DRAFT.getStatus(), teacherProfile.getCertificationStatus());
        assertEquals(BigDecimal.ZERO, teacherProfile.getRatingAvg());
        assertEquals(0, teacherProfile.getReviewCount());
        verify(pointRewardService).reward(userId, TutorPointTaskTypeEnum.PROFILE_INIT,
                "profile_init", "首次初始化身份档案");
    }

}
