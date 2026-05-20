package cn.iocoder.yudao.module.tutor.controller.app.parent.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorTeachModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "用户 App - 家长资料保存 Request VO")
@Data
public class AppTutorParentProfileSaveReqVO {

    @Schema(description = "孩子年级", requiredMode = Schema.RequiredMode.REQUIRED, example = "高三")
    @NotBlank(message = "孩子年级不能为空")
    private String childGrade;

    @Schema(description = "辅导科目，多个用逗号分隔", requiredMode = Schema.RequiredMode.REQUIRED, example = "数学,物理")
    @NotBlank(message = "辅导科目不能为空")
    private String subjects;

    @Schema(description = "每小时最低预算", requiredMode = Schema.RequiredMode.REQUIRED, example = "120")
    @NotNull(message = "最低预算不能为空")
    @Min(value = 0, message = "最低预算不能小于 0")
    @Max(value = 99999, message = "最低预算过高")
    private Integer budgetMin;

    @Schema(description = "每小时最高预算", requiredMode = Schema.RequiredMode.REQUIRED, example = "200")
    @NotNull(message = "最高预算不能为空")
    @Min(value = 0, message = "最高预算不能小于 0")
    @Max(value = 99999, message = "最高预算过高")
    private Integer budgetMax;

    @Schema(description = "授课模式：1 上门，2 在线，3 均可", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "授课模式不能为空")
    @InEnum(TutorTeachModeEnum.class)
    private Integer teachMode;

    @Schema(description = "补充说明", example = "希望老师周末下午上课")
    private String remark;

    @AssertTrue(message = "最高预算不能低于最低预算")
    public boolean isBudgetValid() {
        return budgetMin == null || budgetMax == null || budgetMax >= budgetMin;
    }

}
