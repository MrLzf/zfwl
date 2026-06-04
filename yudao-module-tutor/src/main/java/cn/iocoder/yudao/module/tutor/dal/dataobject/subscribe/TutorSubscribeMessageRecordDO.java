package cn.iocoder.yudao.module.tutor.dal.dataobject.subscribe;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("tutor_subscribe_message_record")
@KeySequence("tutor_subscribe_message_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorSubscribeMessageRecordDO extends TenantBaseDO {
    @TableId
    private Long id;
    private Long userId;
    private String noticeType;
    private String title;
    private String content;
    private String bizId;
    private String targetType;
    private Long targetId;
    /**
     * 0 待发送，10 已发送，20 失败，30 用户未订阅。
     */
    private Integer status;
}
