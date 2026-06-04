package cn.iocoder.yudao.module.tutor.controller.app.value.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AppTutorValueServiceBuyReqVO {
    @NotNull(message = "配置编号不能为空")
    private Long configId;
    @NotBlank(message = "目标类型不能为空")
    private String targetType;
    @NotNull(message = "目标编号不能为空")
    private Long targetId;
}
