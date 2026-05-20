package cn.iocoder.yudao.module.tutor.controller.app.demand.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "用户 App - 家长需求 Response VO")
@Data
@Builder
public class AppTutorDemandRespVO {

    private Long id;
    private Long userId;
    private String title;
    private String grade;
    private String subjects;
    private Integer teachMode;
    private String teachModeName;
    private Integer budgetMin;
    private Integer budgetMax;
    private String description;
    private String cityCode;
    private String cityName;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Boolean distanceVisible;
    private String contactMobileMask;
    private String contactWechatMask;
    private Integer status;
    private String statusName;
    private Integer auditStatus;
    private String auditStatusName;
    private String rejectReason;
    private Integer viewCount;
    private Integer contactViewCount;
    private Integer matchCount;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;

}
