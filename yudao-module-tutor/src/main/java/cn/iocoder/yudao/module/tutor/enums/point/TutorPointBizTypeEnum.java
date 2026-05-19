package cn.iocoder.yudao.module.tutor.enums.point;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 家教积分业务类型枚举。
 */
@RequiredArgsConstructor
@Getter
public enum TutorPointBizTypeEnum implements ArrayValuable<Integer> {

    REGISTER_REWARD(101, "新用户注册奖励", true),
    FIRST_PUBLISH_REWARD(102, "首次发布奖励", true),
    INVITE_REWARD(103, "邀请奖励", true),
    FIVE_STAR_REVIEW_REWARD(104, "五星好评奖励", true),
    VIEW_CONTACT(201, "查看联系方式", false),
    VALUE_SERVICE(202, "购买增值服务", false),
    ADMIN_RECHARGE(301, "后台充值", true);

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TutorPointBizTypeEnum::getType).toArray(Integer[]::new);

    private final Integer type;
    private final String name;
    private final boolean add;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
