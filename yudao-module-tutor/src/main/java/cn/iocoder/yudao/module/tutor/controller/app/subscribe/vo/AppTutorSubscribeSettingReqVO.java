package cn.iocoder.yudao.module.tutor.controller.app.subscribe.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AppTutorSubscribeSettingReqVO {
    @NotBlank(message = "通知类型不能为空")
    private String noticeType;
    @NotNull(message = "订阅状态不能为空")
    private Boolean enabled;
}
