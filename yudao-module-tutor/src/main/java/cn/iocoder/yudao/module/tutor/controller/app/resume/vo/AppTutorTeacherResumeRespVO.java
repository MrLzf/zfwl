package cn.iocoder.yudao.module.tutor.controller.app.resume.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "用户 App - 教师简历 Response VO")
@Data
@Builder
public class AppTutorTeacherResumeRespVO {

    private Long id;
    private Long userId;
    private String title;
    private String subjects;
    private String teachModes;
    private Integer hourlyPrice;
    private Boolean freeTrialEnabled;
    private Integer freeTrialMinutes;
    private String teachingExperience;
    private String availableTimes;
    private String cityCode;
    private String cityName;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal distanceKm;
    private Integer serviceRadiusKm;
    private String contactMobileMask;
    private String contactWechatMask;
    private Integer status;
    private String statusName;
    private Integer auditStatus;
    private String auditStatusName;
    private String rejectReason;
    private BigDecimal ratingAvg;
    private Integer reviewCount;
    private Integer viewCount;
    private Integer contactViewCount;
    private Integer matchCount;
    private LocalDateTime createTime;

}
