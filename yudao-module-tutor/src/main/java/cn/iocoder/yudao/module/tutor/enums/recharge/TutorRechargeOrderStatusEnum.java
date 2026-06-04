package cn.iocoder.yudao.module.tutor.enums.recharge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TutorRechargeOrderStatusEnum {

    WAIT_PAY(0, "待支付"),
    PAID(1, "已支付"),
    EXPIRED(2, "已过期");

    private final Integer status;
    private final String name;
}
