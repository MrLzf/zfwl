package cn.iocoder.yudao.module.tutor.dal.dataobject.message;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("tutor_chat_message")
@KeySequence("tutor_chat_message_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorChatMessageDO extends TenantBaseDO {
    @TableId
    private Long id;
    private Long userId;
    private Long receiverUserId;
    private String messageType;
    private String content;
    private String imageUrl;
    private Boolean readStatus;
}
