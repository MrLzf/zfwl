package cn.iocoder.yudao.module.tutor.controller.app.demand.vo;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.framework.common.validation.Mobile;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorTeachModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "用户 App - 家长需求保存 Request VO")
@Data
public class AppTutorDemandSaveReqVO {

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "高三物理冲刺辅导")
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "年级", requiredMode = Schema.RequiredMode.REQUIRED, example = "高三")
    @NotBlank(message = "年级不能为空")
    private String grade;

    @Schema(description = "科目，多个用逗号分隔", requiredMode = Schema.RequiredMode.REQUIRED, example = "物理,数学")
    @NotBlank(message = "科目不能为空")
    private String subjects;

    @Schema(description = "授课模式：1 上门，2 在线，3 均可", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "授课模式不能为空")
    @InEnum(TutorTeachModeEnum.class)
    private Integer teachMode;

    @Schema(description = "详细上课地址，上门或均可模式必填", example = "朝阳区望京街道花家地小区")
    @Size(max = 255, message = "上课地址不能超过 255 个字符")
    private String address;

    @Schema(description = "最低预算", requiredMode = Schema.RequiredMode.REQUIRED, example = "120")
    @NotNull(message = "最低预算不能为空")
    @Min(value = 0, message = "最低预算不能小于 0")
    @Max(value = 99999, message = "最低预算过高")
    private Integer budgetMin;

    @Schema(description = "最高预算", requiredMode = Schema.RequiredMode.REQUIRED, example = "240")
    @NotNull(message = "最高预算不能为空")
    @Min(value = 0, message = "最高预算不能小于 0")
    @Max(value = 99999, message = "最高预算过高")
    private Integer budgetMax;

    @Schema(description = "详细要求", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "详细要求不能为空")
    private String description;

    @Schema(description = "城市编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "110100")
    @NotBlank(message = "城市不能为空")
    private String cityCode;

    @Schema(description = "经度", example = "116.397128")
    @DecimalMin(value = "-180.0", message = "经度范围不正确")
    @DecimalMax(value = "180.0", message = "经度范围不正确")
    private BigDecimal longitude;

    @Schema(description = "纬度", example = "39.916527")
    @DecimalMin(value = "-90.0", message = "纬度范围不正确")
    @DecimalMax(value = "90.0", message = "纬度范围不正确")
    private BigDecimal latitude;

    @Schema(description = "是否显示距离", example = "true")
    private Boolean distanceVisible;

    @Schema(description = "联系手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "联系手机号不能为空")
    @Mobile
    private String contactMobile;

    @Schema(description = "联系微信号")
    private String contactWechat;

    @AssertTrue(message = "最高预算不能低于最低预算")
    public boolean isBudgetValid() {
        return budgetMin == null || budgetMax == null || budgetMax >= budgetMin;
    }

    @AssertTrue(message = "上门或均可模式必须填写上课地址")
    public boolean isAddressValid() {
        return TutorTeachModeEnum.ONLINE.getMode().equals(teachMode) || StrUtil.isNotBlank(address);
    }

}
