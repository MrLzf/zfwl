package cn.iocoder.yudao.module.tutor.dal.dataobject.certification;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 教师认证 DO。
 */
@TableName("tutor_certification")
@KeySequence("tutor_certification_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorCertificationDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private Long teacherProfileId;
    private String realName;
    private String idCardNoEncrypt;
    private String idCardNoMask;
    private String educationFileUrl;
    private String teacherCertFileUrl;
    /**
     * 枚举 {@link TutorAuditStatusEnum}
     */
    private Integer status;
    private String rejectReason;
    private Long auditorId;
    private LocalDateTime auditTime;

}
