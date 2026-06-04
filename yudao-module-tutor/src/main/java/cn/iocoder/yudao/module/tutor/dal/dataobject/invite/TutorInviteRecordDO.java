package cn.iocoder.yudao.module.tutor.dal.dataobject.invite;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("tutor_invite_record")
@KeySequence("tutor_invite_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorInviteRecordDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long inviterUserId;
    private Long inviteeUserId;
    private String inviteCode;
    private String deviceId;
    private String ip;
    private Integer rewardPoint;
}
