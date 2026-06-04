package cn.iocoder.yudao.module.tutor.controller.app.message.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class AppTutorMessagePageReqVO extends PageParam {

    @Schema(description = "消息分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "audit")
    @NotBlank(message = "消息分类不能为空")
    @Pattern(regexp = "audit|contact|match|review|point", message = "消息分类不正确")
    private String category;

}
