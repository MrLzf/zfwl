package cn.iocoder.yudao.module.member.enums.point;

import cn.hutool.core.util.EnumUtil;
import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 会员积分的业务类型枚举
 *
 * @author 芋道源码
 */
@AllArgsConstructor
@Getter
public enum MemberPointBizTypeEnum implements ArrayValuable<Integer> {

    SIGN(1, "签到", "签到获得 {} 积分", true),
    ADMIN(2, "管理员修改", "管理员修改 {} 积分", true),

    ORDER_USE(11, "订单积分抵扣", "下单使用 {} 积分", false), // 下单时，扣减积分
    ORDER_USE_CANCEL(12, "订单积分抵扣（整单取消）", "订单取消，退还 {} 积分", true), // ORDER_USE 的取消
    ORDER_USE_CANCEL_ITEM(13, "订单积分抵扣（单个退款）", "订单退款，退还 {} 积分", true), // ORDER_USE 的取消

    ORDER_GIVE(21, "订单积分奖励", "下单获得 {} 积分", true), // 支付订单时，赠送积分
    ORDER_GIVE_CANCEL(22, "订单积分奖励（整单取消）", "订单取消，退还 {} 积分", false), // ORDER_GIVE 的取消
    ORDER_GIVE_CANCEL_ITEM(23, "订单积分奖励（单个退款）", "订单退款，扣除赠送的 {} 积分", false), // ORDER_GIVE 的取消

    TUTOR_PROFILE_INIT(101, "家教身份档案奖励", "首次选择家教身份获得 {} 积分", true),
    TUTOR_ROLE_PROFILE_COMPLETE(102, "家教资料完善奖励", "首次完善家教资料获得 {} 积分", true),
    TUTOR_INVITE_REWARD(103, "家教邀请奖励", "邀请新用户获得 {} 积分", true),
    TUTOR_FIVE_STAR_REVIEW(104, "家教五星好评奖励", "家教五星好评获得 {} 积分", true),
    TUTOR_POINT_RECHARGE(105, "家教积分充值", "充值获得 {} 积分", true),
    TUTOR_VIEW_CONTACT(201, "家教查看联系方式", "家教查看联系方式使用 {} 积分", false),
    TUTOR_VALUE_SERVICE(202, "家教增值服务", "购买增值服务使用 {} 积分", false)
    ;

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 名字
     */
    private final String name;
    /**
     * 描述
     */
    private final String description;
    /**
     * 是否为扣减积分
     */
    private final boolean add;

    @Override
    public Integer[] array() {
        return new Integer[0];
    }

    public static MemberPointBizTypeEnum getByType(Integer type) {
        return EnumUtil.getBy(MemberPointBizTypeEnum.class,
                e -> Objects.equals(type, e.getType()));
    }

}
