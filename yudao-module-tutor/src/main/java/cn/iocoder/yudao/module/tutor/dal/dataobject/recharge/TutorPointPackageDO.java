package cn.iocoder.yudao.module.tutor.dal.dataobject.recharge;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("tutor_point_package")
@KeySequence("tutor_point_package_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorPointPackageDO extends TenantBaseDO {

    @TableId
    private Long id;
    private String name;
    private Integer point;
    private Integer bonusPoint;
    private Integer price;
    private Integer status;
    private Integer sort;
}
