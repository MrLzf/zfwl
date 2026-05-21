package cn.iocoder.yudao.module.tutor.controller.app.square.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Schema(description = "用户 App - 教师广场分页 Request VO")
@Data
public class AppTutorTeacherResumePageReqVO extends PageParam {

    @Schema(description = "城市编码", example = "110100")
    private String cityCode;

    @Schema(description = "科目", example = "数学")
    private String subject;

    @Schema(description = "授课模式：1 上门，2 在线，3 均可", example = "1")
    private Integer teachMode;

    @Schema(description = "时薪下限", example = "120")
    @Min(value = 0, message = "价格不能小于 0")
    @Max(value = 99999, message = "价格过高")
    private Integer priceMin;

    @Schema(description = "时薪上限", example = "300")
    @Min(value = 0, message = "价格不能小于 0")
    @Max(value = 99999, message = "价格过高")
    private Integer priceMax;

    @Schema(description = "是否只看支持试课", example = "true")
    private Boolean freeTrialEnabled;

    @Schema(description = "是否只看认证教师", example = "true")
    private Boolean certified;

    @Schema(description = "当前位置经度", example = "116.397128")
    @DecimalMin(value = "-180.0", message = "经度范围不正确")
    @DecimalMax(value = "180.0", message = "经度范围不正确")
    private BigDecimal longitude;

    @Schema(description = "当前位置纬度", example = "39.916527")
    @DecimalMin(value = "-90.0", message = "纬度范围不正确")
    @DecimalMax(value = "90.0", message = "纬度范围不正确")
    private BigDecimal latitude;

    @Schema(description = "最大距离，单位公里", example = "10")
    @Min(value = 0, message = "距离不能小于 0")
    @Max(value = 500, message = "距离过大")
    private Integer distanceKm;

    @Schema(description = "排序：default 综合排序，latest 最新，distance 距离最近", example = "distance")
    private String sortType;

}
