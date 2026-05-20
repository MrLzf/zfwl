package cn.iocoder.yudao.module.tutor.dal.dataobject.resume;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 教师简历 DO。
 */
@TableName("tutor_teacher_resume")
@KeySequence("tutor_teacher_resume_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorTeacherResumeDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String title;
    /**
     * 科目，多个用逗号分隔。
     */
    private String subjects;
    /**
     * 授课模式，多个用逗号分隔。
     */
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
    private Integer serviceRadiusKm;
    private String contactMobileEncrypt;
    private String contactMobileMask;
    private String contactWechatEncrypt;
    private String contactWechatMask;
    /**
     * 枚举 {@link TutorPublishStatusEnum}
     */
    private Integer status;
    /**
     * 枚举 {@link TutorAuditStatusEnum}
     */
    private Integer auditStatus;
    private String rejectReason;
    private BigDecimal ratingAvg;
    private Integer reviewCount;
    private Integer viewCount;
    private Integer contactViewCount;
    private Integer matchCount;
    private LocalDateTime topUntil;
    private LocalDateTime urgentUntil;
    private LocalDateTime boostUntil;

}
