package cn.iocoder.yudao.module.tutor.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "管理后台 - 家教数据概览 Response VO")
@Data
@Builder
public class AdminTutorDashboardSummaryRespVO {

    private Long userCount;
    private Long parentCount;
    private Long teacherCount;
    private Long demandCount;
    private Long resumeCount;
    private Long showingDemandCount;
    private Long showingResumeCount;
    private Long certificationPendingCount;
    private Long demandPendingCount;
    private Long resumePendingCount;
    private Long contactViewCount;
    private Long matchSuccessCount;
    private Long reviewCount;

}
