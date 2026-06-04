package cn.iocoder.yudao.module.tutor.enums.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TutorValueServiceTypeEnum {

    BOOST("boost", "加速"),
    URGENT("urgent", "加急"),
    TOP("top", "置顶"),
    VIP("vip", "VIP 会员");

    private final String type;
    private final String name;
}
