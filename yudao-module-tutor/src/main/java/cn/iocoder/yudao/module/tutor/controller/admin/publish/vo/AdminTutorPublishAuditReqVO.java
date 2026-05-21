package cn.iocoder.yudao.module.tutor.controller.admin.publish.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 家教发布内容审核 Request VO")
@Data
public class AdminTutorPublishAuditReqVO {

    @Schema(description = "发布内容编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "发布内容编号不能为空")
    private Long id;

    @Schema(description = "审核状态：20 通过，30 拒绝", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull(message = "审核状态不能为空")
    @InEnum(TutorAuditStatusEnum.class)
    private Integer auditStatus;

    @Schema(description = "拒绝原因", example = "联系方式填写不完整")
    private String rejectReason;

    @AssertTrue(message = "只允许审核通过或拒绝")
    public boolean isAuditStatusValid() {
        return TutorAuditStatusEnum.APPROVED.getStatus().equals(auditStatus)
                || TutorAuditStatusEnum.REJECTED.getStatus().equals(auditStatus);
    }

    @AssertTrue(message = "拒绝发布内容时必须填写原因")
    public boolean isRejectReasonValid() {
        return !TutorAuditStatusEnum.REJECTED.getStatus().equals(auditStatus)
                || (rejectReason != null && !rejectReason.trim().isEmpty());
    }

}
