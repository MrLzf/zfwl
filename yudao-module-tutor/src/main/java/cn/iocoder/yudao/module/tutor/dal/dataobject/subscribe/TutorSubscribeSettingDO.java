package cn.iocoder.yudao.module.tutor.dal.dataobject.subscribe;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("tutor_subscribe_setting")
@KeySequence("tutor_subscribe_setting_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorSubscribeSettingDO extends TenantBaseDO {
    @TableId
    private Long id;
    private Long userId;
    private String noticeType;
    private Boolean enabled;
}
