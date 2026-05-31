package cn.iocoder.yudao.module.tutor.service.point;

import cn.iocoder.yudao.module.member.service.signin.MemberSignInRecordService;
import cn.iocoder.yudao.module.tutor.controller.app.point.vo.AppTutorPointTaskRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.profile.TutorUserProfileMapper;
import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Validated
public class TutorPointTaskServiceImpl implements TutorPointTaskService {

    @Resource
    private MemberSignInRecordService signInRecordService;
    @Resource
    private TutorUserProfileMapper userProfileMapper;
    @Resource
    private TutorPointRewardService pointRewardService;

    @Override
    public List<AppTutorPointTaskRespVO> getTaskList(Long userId) {
        boolean todaySignedIn = Boolean.TRUE.equals(
                signInRecordService.getSignInRecordSummary(userId).getTodaySignIn());
        TutorUserProfileDO profile = userProfileMapper.selectByUserId(userId);
        String roleProfilePath = getRoleProfilePath(profile);
        return Arrays.asList(
                task("daily_sign_in", "每日签到", "每天签到领取积分", null, todaySignedIn, "sign_in", ""),
                task("profile_init", "选择家教身份", "首次选择家长或教师身份", 20,
                        profile != null, "navigate", "/pages/tutor/identity/index"),
                task("role_profile_complete", "完善对应资料", "首次完善对应身份资料", 30,
                        isRoleProfileComplete(userId, profile), "navigate", roleProfilePath),
                task("five_star_review", "五星评价", "五星评价双方各得积分", 10,
                        false, "navigate", "/pages/tutor/reviews/index"));
    }

    private boolean isRoleProfileComplete(Long userId, TutorUserProfileDO profile) {
        if (profile == null) {
            return false;
        }
        return pointRewardService.hasReward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE);
    }

    private String getRoleProfilePath(TutorUserProfileDO profile) {
        if (profile == null) {
            return "/pages/tutor/identity/index";
        }
        if (Objects.equals(profile.getRole(), TutorUserRoleEnum.PARENT.getRole())) {
            return "/pages/tutor/parent-profile/index";
        }
        if (Objects.equals(profile.getRole(), TutorUserRoleEnum.TEACHER.getRole())) {
            return "/pages/tutor/teacher-profile/index";
        }
        return "/pages/tutor/identity/index";
    }

    private AppTutorPointTaskRespVO task(String type, String title, String description, Integer point,
                                         boolean completed, String action, String path) {
        return AppTutorPointTaskRespVO.builder().type(type).title(title).description(description).point(point)
                .completed(completed).action(action).path(path).build();
    }

}
