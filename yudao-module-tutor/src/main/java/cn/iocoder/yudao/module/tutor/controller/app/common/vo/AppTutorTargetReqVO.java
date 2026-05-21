package cn.iocoder.yudao.module.tutor.controller.app.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "用户 App - 家教目标 Request VO")
@Data
public class AppTutorTargetReqVO {

    @Schema(description = "目标类型：demand 家长需求，resume 教师简历", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "目标类型不能为空")
    private String targetType;

    @Schema(description = "目标编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "目标编号不能为空")
    private Long targetId;

}
