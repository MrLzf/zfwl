package cn.iocoder.yudao.module.tutor.enums.target;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 家教互动目标类型。
 */
@RequiredArgsConstructor
@Getter
public enum TutorTargetTypeEnum {

    DEMAND("demand", "家长需求"),
    RESUME("resume", "教师简历");

    private final String type;
    private final String name;

    public static boolean isDemand(String type) {
        return DEMAND.getType().equals(type);
    }

    public static boolean isResume(String type) {
        return RESUME.getType().equals(type);
    }

}
