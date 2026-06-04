package cn.iocoder.yudao.module.tutor.dal.dataobject.vip;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("tutor_vip_record")
@KeySequence("tutor_vip_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorVipRecordDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private Long configId;
    private Integer pointPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer monthlyGiftPoint;
    private LocalDateTime lastGiftTime;
    /**
     * 0 生效，1 失效。
     */
    private Integer status;
}
