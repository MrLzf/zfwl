package cn.iocoder.yudao.module.tutor.controller.app.recharge.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppTutorPointRechargeCreateReqVO {
    @NotNull(message = "积分包编号不能为空")
    private Long packageId;
}
