package cn.iocoder.yudao.module.tutor.dal.dataobject.contact;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.tutor.enums.target.TutorTargetTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("tutor_contact_view_record")
@KeySequence("tutor_contact_view_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorContactViewRecordDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long viewerUserId;
    /**
     * 枚举 {@link TutorTargetTypeEnum}
     */
    private String targetType;
    private Long targetId;
    private Long targetUserId;
    private Integer pointCost;
    private LocalDateTime freeReuseUntil;
    private Boolean viewedMobile;
    private Boolean viewedWechat;

}
