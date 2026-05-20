package cn.iocoder.yudao.module.tutor.controller.app.teacher.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;

@Schema(description = "用户 App - 教师资料保存 Request VO")
@Data
public class AppTutorTeacherProfileSaveReqVO {

    @Schema(description = "最高学历", requiredMode = Schema.RequiredMode.REQUIRED, example = "硕士")
    @NotBlank(message = "最高学历不能为空")
    private String educationLevel;

    @Schema(description = "学校", example = "浙江大学")
    private String schoolName;

    @Schema(description = "专业", example = "数学")
    private String major;

    @Schema(description = "是否有教师资格证", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否有教师资格证不能为空")
    private Boolean hasTeacherCertificate;

    @Schema(description = "可授科目，多个用逗号分隔", requiredMode = Schema.RequiredMode.REQUIRED, example = "数学,物理")
    @NotBlank(message = "可授科目不能为空")
    private String subjects;

    @Schema(description = "授课模式，多个用逗号分隔：1 上门，2 在线，3 均可", requiredMode = Schema.RequiredMode.REQUIRED, example = "1,2")
    @NotBlank(message = "授课模式不能为空")
    private String teachModes;

    @Schema(description = "最低时薪", requiredMode = Schema.RequiredMode.REQUIRED, example = "150")
    @NotNull(message = "最低时薪不能为空")
    @Min(value = 0, message = "最低时薪不能小于 0")
    @Max(value = 99999, message = "最低时薪过高")
    private Integer hourlyPriceMin;

    @Schema(description = "最高时薪", requiredMode = Schema.RequiredMode.REQUIRED, example = "300")
    @NotNull(message = "最高时薪不能为空")
    @Min(value = 0, message = "最高时薪不能小于 0")
    @Max(value = 99999, message = "最高时薪过高")
    private Integer hourlyPriceMax;

    @Schema(description = "服务半径，单位公里", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "服务半径不能为空")
    @Min(value = 0, message = "服务半径不能小于 0")
    @Max(value = 200, message = "服务半径过大")
    private Integer serviceRadiusKm;

    @Schema(description = "是否支持试课", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否支持试课不能为空")
    private Boolean freeTrialEnabled;

    @Schema(description = "试课时长，单位分钟", example = "30")
    @Min(value = 0, message = "试课时长不能小于 0")
    @Max(value = 240, message = "试课时长过长")
    private Integer freeTrialMinutes;

    @Schema(description = "教龄", example = "3")
    @Min(value = 0, message = "教龄不能小于 0")
    @Max(value = 80, message = "教龄过大")
    private Integer teachingYears;

    @Schema(description = "教学介绍", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "教学介绍不能为空")
    private String intro;

    @AssertTrue(message = "最高时薪不能低于最低时薪")
    public boolean isHourlyPriceValid() {
        return hourlyPriceMin == null || hourlyPriceMax == null || hourlyPriceMax >= hourlyPriceMin;
    }

    @AssertTrue(message = "开启试课时必须填写试课时长")
    public boolean isFreeTrialMinutesValid() {
        return !Boolean.TRUE.equals(freeTrialEnabled) || freeTrialMinutes != null;
    }

}
