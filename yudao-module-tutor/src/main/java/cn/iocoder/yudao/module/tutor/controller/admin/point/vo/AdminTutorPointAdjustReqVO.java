package cn.iocoder.yudao.module.tutor.controller.admin.point.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 家教积分调整 Request VO")
@Data
public class AdminTutorPointAdjustReqVO {

    @Schema(description = "会员用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "变动积分，正数增加，负数扣减", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull(message = "变动积分不能为空")
    private Integer point;

    @Schema(description = "调整备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "线下活动补偿")
    @NotBlank(message = "调整备注不能为空")
    private String remark;

    @AssertTrue(message = "变动积分不能为 0")
    public boolean isPointValid() {
        return point != null && point != 0;
    }

}
