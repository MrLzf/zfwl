package cn.iocoder.yudao.module.tutor.dal.dataobject.point;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("tutor_point_adjust_record")
@KeySequence("tutor_point_adjust_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorPointAdjustRecordDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private Integer point;
    private String remark;
    private Long operatorId;

}
