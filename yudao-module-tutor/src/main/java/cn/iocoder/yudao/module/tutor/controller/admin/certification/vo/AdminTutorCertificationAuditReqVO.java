package cn.iocoder.yudao.module.tutor.controller.admin.certification.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 教师认证审核 Request VO")
@Data
public class AdminTutorCertificationAuditReqVO {

    @Schema(description = "认证编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "认证编号不能为空")
    private Long id;

    @Schema(description = "审核状态：20 通过，30 拒绝", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull(message = "审核状态不能为空")
    @InEnum(TutorAuditStatusEnum.class)
    private Integer status;

    @Schema(description = "拒绝原因", example = "材料不清晰")
    private String rejectReason;

    @AssertTrue(message = "只允许审核通过或拒绝")
    public boolean isAuditStatusValid() {
        return TutorAuditStatusEnum.APPROVED.getStatus().equals(status)
                || TutorAuditStatusEnum.REJECTED.getStatus().equals(status);
    }

    @AssertTrue(message = "拒绝认证时必须填写原因")
    public boolean isRejectReasonValid() {
        return !TutorAuditStatusEnum.REJECTED.getStatus().equals(status)
                || (rejectReason != null && !rejectReason.trim().isEmpty());
    }

}
