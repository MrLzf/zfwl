package cn.iocoder.yudao.module.tutor.dal.dataobject.invite;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("tutor_invite_user")
@KeySequence("tutor_invite_user_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorInviteUserDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String inviteCode;
    private String inviteLink;
    private LocalDateTime linkExpireTime;
}
