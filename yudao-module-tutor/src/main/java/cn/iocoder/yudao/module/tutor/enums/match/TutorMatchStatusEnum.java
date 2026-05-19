package cn.iocoder.yudao.module.tutor.enums.match;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 家教匹配状态枚举。
 */
@RequiredArgsConstructor
@Getter
public enum TutorMatchStatusEnum implements ArrayValuable<Integer> {

    CONTACT_VIEWED(10, "已交换联系方式"),
    PARENT_CONFIRMED(20, "家长已确认"),
    TEACHER_CONFIRMED(30, "教师已确认"),
    BOTH_CONFIRMED(40, "双方已确认"),
    CANCELED(50, "已取消");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TutorMatchStatusEnum::getStatus).toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
