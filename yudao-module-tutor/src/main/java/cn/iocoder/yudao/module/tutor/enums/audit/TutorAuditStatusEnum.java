package cn.iocoder.yudao.module.tutor.enums.audit;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 家教审核状态枚举。
 */
@RequiredArgsConstructor
@Getter
public enum TutorAuditStatusEnum implements ArrayValuable<Integer> {

    DRAFT(0, "待提交"),
    WAITING(10, "待审核"),
    APPROVED(20, "审核通过"),
    REJECTED(30, "审核拒绝");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TutorAuditStatusEnum::getStatus).toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
