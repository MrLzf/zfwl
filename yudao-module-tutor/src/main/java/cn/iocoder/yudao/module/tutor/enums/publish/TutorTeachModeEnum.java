package cn.iocoder.yudao.module.tutor.enums.publish;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 授课模式枚举。
 */
@RequiredArgsConstructor
@Getter
public enum TutorTeachModeEnum implements ArrayValuable<Integer> {

    HOME(1, "上门"),
    ONLINE(2, "在线"),
    BOTH(3, "均可");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TutorTeachModeEnum::getMode).toArray(Integer[]::new);

    private final Integer mode;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
