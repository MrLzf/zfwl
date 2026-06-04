package cn.iocoder.yudao.module.tutor.dal.dataobject.recharge;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("tutor_point_recharge_order")
@KeySequence("tutor_point_recharge_order_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorPointRechargeOrderDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private Long packageId;
    private String packageName;
    private Integer point;
    private Integer bonusPoint;
    private Integer totalPoint;
    private Integer price;
    private Integer status;
    private String merchantOrderId;
    private Long payOrderId;
    private String payChannelCode;
    private LocalDateTime payTime;
    private LocalDateTime expireTime;
}
