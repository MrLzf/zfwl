package cn.iocoder.yudao.module.tutor.dal.dataobject.teacher;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 教师资料 DO。
 */
@TableName("tutor_teacher_profile")
@KeySequence("tutor_teacher_profile_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorTeacherProfileDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private Long profileId;
    private String educationLevel;
    private String schoolName;
    private String major;
    private Boolean hasTeacherCertificate;
    /**
     * 可授科目，多个用逗号分隔。
     */
    private String subjects;
    /**
     * 授课模式，多个用逗号分隔。
     */
    private String teachModes;
    private Integer hourlyPriceMin;
    private Integer hourlyPriceMax;
    private Integer serviceRadiusKm;
    private Boolean freeTrialEnabled;
    private Integer freeTrialMinutes;
    private Integer teachingYears;
    private String intro;
    /**
     * 枚举 {@link TutorAuditStatusEnum}
     */
    private Integer certificationStatus;
    private BigDecimal ratingAvg;
    private Integer reviewCount;

}
