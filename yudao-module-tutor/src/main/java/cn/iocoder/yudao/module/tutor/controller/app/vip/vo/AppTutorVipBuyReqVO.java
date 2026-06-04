package cn.iocoder.yudao.module.tutor.controller.app.vip.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppTutorVipBuyReqVO {
    @NotNull(message = "VIP 配置不能为空")
    private Long configId;
}
