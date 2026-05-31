package cn.iocoder.yudao.module.tutor.enums.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TutorPointTaskTypeEnum {

    PROFILE_INIT("profile_init", 20, "选择家教身份"),
    ROLE_PROFILE_COMPLETE("role_profile_complete", 30, "完善对应资料"),
    FIVE_STAR_REVIEW("five_star_review", 10, "五星评价");

    private final String type;
    private final Integer point;
    private final String title;

}
