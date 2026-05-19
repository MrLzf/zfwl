package cn.iocoder.yudao.module.tutor.enums.profile;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 家教用户身份枚举。
 */
@RequiredArgsConstructor
@Getter
public enum TutorUserRoleEnum implements ArrayValuable<Integer> {

    PARENT(1, "家长"),
    TEACHER(2, "教师");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TutorUserRoleEnum::getRole).toArray(Integer[]::new);

    private final Integer role;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
