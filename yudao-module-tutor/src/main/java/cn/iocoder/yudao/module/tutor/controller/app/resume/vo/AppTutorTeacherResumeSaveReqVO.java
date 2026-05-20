package cn.iocoder.yudao.module.tutor.controller.app.resume.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorTeachModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "用户 App - 教师简历保存 Request VO")
@Data
public class AppTutorTeacherResumeSaveReqVO {

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "985硕士数学老师")
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "科目，多个用逗号分隔", requiredMode = Schema.RequiredMode.REQUIRED, example = "数学,物理")
    @NotBlank(message = "科目不能为空")
    private String subjects;

    @Schema(description = "授课模式，多个用逗号分隔：1 上门，2 在线，3 均可", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "授课模式不能为空")
    private String teachModes;

    @Schema(description = "时薪", requiredMode = Schema.RequiredMode.REQUIRED, example = "200")
    @NotNull(message = "时薪不能为空")
    @Min(value = 0, message = "时薪不能小于 0")
    @Max(value = 99999, message = "时薪过高")
    private Integer hourlyPrice;

    @Schema(description = "是否支持试课", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否支持试课不能为空")
    private Boolean freeTrialEnabled;

    @Schema(description = "试课时长", example = "30")
    @Min(value = 0, message = "试课时长不能小于 0")
    @Max(value = 240, message = "试课时长过长")
    private Integer freeTrialMinutes;

    @Schema(description = "教学经验", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "教学经验不能为空")
    private String teachingExperience;

    @Schema(description = "可授课时间 JSON 或文本")
    private String availableTimes;

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

    @Schema(description = "服务半径，单位公里", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "服务半径不能为空")
    @Min(value = 0, message = "服务半径不能小于 0")
    @Max(value = 200, message = "服务半径过大")
    private Integer serviceRadiusKm;

    @Schema(description = "联系手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "联系手机号不能为空")
    @Mobile
    private String contactMobile;

    @Schema(description = "联系微信号")
    private String contactWechat;

    @AssertTrue(message = "开启试课时必须填写试课时长")
    public boolean isFreeTrialMinutesValid() {
        return !Boolean.TRUE.equals(freeTrialEnabled) || freeTrialMinutes != null;
    }

    @AssertTrue(message = "授课模式不正确")
    public boolean isTeachModesValid() {
        if (teachModes == null) {
            return true;
        }
        for (String teachMode : teachModes.split(",")) {
            boolean matched = false;
            for (TutorTeachModeEnum modeEnum : TutorTeachModeEnum.values()) {
                if (modeEnum.getMode().toString().equals(teachMode.trim())) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }
        return true;
    }

}
