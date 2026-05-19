package cn.iocoder.yudao.module.tutor.controller.app.profile.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "用户 App - 家教档案初始化 Request VO")
@Data
public class AppTutorProfileInitReqVO {

    @Schema(description = "身份：1 家长，2 教师", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "身份不能为空")
    @InEnum(TutorUserRoleEnum.class)
    private Integer role;

    @Schema(description = "城市编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "110100")
    @NotBlank(message = "城市不能为空")
    private String cityCode;

}
