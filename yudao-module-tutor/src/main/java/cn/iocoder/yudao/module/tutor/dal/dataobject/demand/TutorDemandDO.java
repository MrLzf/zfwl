package cn.iocoder.yudao.module.tutor.dal.dataobject.demand;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorTeachModeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 家长需求 DO。
 */
@TableName("tutor_demand")
@KeySequence("tutor_demand_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorDemandDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String grade;
    /**
     * 科目，多个用逗号分隔。
     */
    private String subjects;
    /**
     * 枚举 {@link TutorTeachModeEnum}
     */
    private Integer teachMode;
    private String address;
    private Integer budgetMin;
    private Integer budgetMax;
    private String description;
    private String cityCode;
    private String cityName;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Boolean distanceVisible;
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
    private Integer viewCount;
    private Integer contactViewCount;
    private Integer matchCount;
    private LocalDateTime expireTime;
    private LocalDateTime topUntil;
    private LocalDateTime urgentUntil;
    private LocalDateTime boostUntil;

}
