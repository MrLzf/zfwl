package cn.iocoder.yudao.module.tutor.controller.app.teacher.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "用户 App - 教师资料 Response VO")
@Data
@Builder
public class AppTutorTeacherProfileRespVO {

    private Long id;
    private Long userId;
    private Long profileId;
    private String educationLevel;
    private String schoolName;
    private String major;
    private Boolean hasTeacherCertificate;
    private String subjects;
    private String teachModes;
    private Integer hourlyPriceMin;
    private Integer hourlyPriceMax;
    private Integer serviceRadiusKm;
    private Boolean freeTrialEnabled;
    private Integer freeTrialMinutes;
    private Integer teachingYears;
    private String intro;
    private Integer certificationStatus;
    private String certificationStatusName;
    private BigDecimal ratingAvg;
    private Integer reviewCount;

}
