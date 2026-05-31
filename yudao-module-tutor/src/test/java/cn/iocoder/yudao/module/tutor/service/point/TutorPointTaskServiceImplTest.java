package cn.iocoder.yudao.module.tutor.service.point;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.member.controller.app.signin.vo.record.AppMemberSignInRecordSummaryRespVO;
import cn.iocoder.yudao.module.member.service.signin.MemberSignInRecordService;
import cn.iocoder.yudao.module.tutor.controller.app.point.vo.AppTutorPointTaskRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.parent.TutorParentProfileDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.parent.TutorParentProfileMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.profile.TutorUserProfileMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.teacher.TutorTeacherProfileMapper;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TutorPointTaskServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorPointTaskServiceImpl taskService;
    @Mock
    private MemberSignInRecordService signInRecordService;
    @Mock
    private TutorUserProfileMapper userProfileMapper;
    @Mock
    private TutorParentProfileMapper parentProfileMapper;
    @Mock
    private TutorTeacherProfileMapper teacherProfileMapper;
    @Mock
    private TutorPointRewardService pointRewardService;

    @Test
    void getTaskList_whenParent_returnsSignInAndParentCompletion() {
        Long userId = 100L;
        when(signInRecordService.getSignInRecordSummary(userId)).thenReturn(summary(true));
        when(userProfileMapper.selectByUserId(userId)).thenReturn(TutorUserProfileDO.builder()
                .role(TutorUserRoleEnum.PARENT.getRole()).build());
        when(pointRewardService.hasReward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE)).thenReturn(true);

        List<AppTutorPointTaskRespVO> tasks = taskService.getTaskList(userId);

        assertTrue(find(tasks, "daily_sign_in").getCompleted());
        assertTrue(find(tasks, "profile_init").getCompleted());
        assertTrue(find(tasks, "role_profile_complete").getCompleted());
        assertEquals("/pages/tutor/parent-profile/index", find(tasks, "role_profile_complete").getPath());
        assertEquals("/pages/tutor/reviews/index", find(tasks, "five_star_review").getPath());
    }

    @Test
    void getTaskList_whenTeacherDraftExistsButNotRewarded_returnsIncompleteTeacherPath() {
        Long userId = 100L;
        when(signInRecordService.getSignInRecordSummary(userId)).thenReturn(summary(false));
        when(userProfileMapper.selectByUserId(userId)).thenReturn(TutorUserProfileDO.builder()
                .role(TutorUserRoleEnum.TEACHER.getRole()).build());

        List<AppTutorPointTaskRespVO> tasks = taskService.getTaskList(userId);

        assertFalse(find(tasks, "daily_sign_in").getCompleted());
        assertFalse(find(tasks, "role_profile_complete").getCompleted());
        assertEquals("/pages/tutor/teacher-profile/index", find(tasks, "role_profile_complete").getPath());
    }

    @Test
    void getTaskList_whenNoProfile_returnsIdentityPath() {
        Long userId = 100L;
        when(signInRecordService.getSignInRecordSummary(userId)).thenReturn(summary(false));

        List<AppTutorPointTaskRespVO> tasks = taskService.getTaskList(userId);

        assertFalse(find(tasks, "profile_init").getCompleted());
        assertFalse(find(tasks, "role_profile_complete").getCompleted());
        assertEquals("/pages/tutor/identity/index", find(tasks, "role_profile_complete").getPath());
    }

    private AppMemberSignInRecordSummaryRespVO summary(boolean todaySignIn) {
        AppMemberSignInRecordSummaryRespVO summary = new AppMemberSignInRecordSummaryRespVO();
        summary.setTodaySignIn(todaySignIn);
        return summary;
    }

    private AppTutorPointTaskRespVO find(List<AppTutorPointTaskRespVO> tasks, String type) {
        return tasks.stream().filter(task -> type.equals(task.getType())).findFirst().orElseThrow(AssertionError::new);
    }

}
