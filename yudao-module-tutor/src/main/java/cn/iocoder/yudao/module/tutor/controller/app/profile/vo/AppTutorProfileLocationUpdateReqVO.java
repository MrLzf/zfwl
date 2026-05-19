package cn.iocoder.yudao.module.tutor.controller.app.profile.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "用户 App - 家教档案定位更新 Request VO")
@Data
public class AppTutorProfileLocationUpdateReqVO {

    @Schema(description = "城市编码", example = "110100")
    private String cityCode;

    @Schema(description = "经度", requiredMode = Schema.RequiredMode.REQUIRED, example = "116.397128")
    @NotNull(message = "经度不能为空")
    @DecimalMin(value = "-180.0", message = "经度范围不正确")
    @DecimalMax(value = "180.0", message = "经度范围不正确")
    private BigDecimal longitude;

    @Schema(description = "纬度", requiredMode = Schema.RequiredMode.REQUIRED, example = "39.916527")
    @NotNull(message = "纬度不能为空")
    @DecimalMin(value = "-90.0", message = "纬度范围不正确")
    @DecimalMax(value = "90.0", message = "纬度范围不正确")
    private BigDecimal latitude;

    @Schema(description = "定位地址", example = "北京市东城区")
    private String locationAddress;

}
