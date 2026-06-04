package cn.iocoder.yudao.module.tutor.dal.dataobject.value;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("tutor_value_service_config")
@KeySequence("tutor_value_service_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorValueServiceConfigDO extends TenantBaseDO {

    @TableId
    private Long id;
    private String serviceType;
    private String targetType;
    private Integer pointPrice;
    private Integer durationHours;
    private Integer status;
}
