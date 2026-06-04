package cn.iocoder.yudao.module.tutor.dal.dataobject.value;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("tutor_value_service_order")
@KeySequence("tutor_value_service_order_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorValueServiceOrderDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private Long configId;
    private String serviceType;
    private String targetType;
    private Long targetId;
    private Integer pointPrice;
    private LocalDateTime effectStartTime;
    private LocalDateTime effectEndTime;
    private Integer status;
}
