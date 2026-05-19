package cn.iocoder.yudao.module.tutor.enums.publish;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 家教发布状态枚举。
 */
@RequiredArgsConstructor
@Getter
public enum TutorPublishStatusEnum implements ArrayValuable<Integer> {

    DRAFT(0, "草稿"),
    WAIT_AUDIT(10, "待审核"),
    SHOWING(20, "展示中"),
    REJECTED(30, "审核拒绝"),
    OFFLINE(40, "已下架");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TutorPublishStatusEnum::getStatus).toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
