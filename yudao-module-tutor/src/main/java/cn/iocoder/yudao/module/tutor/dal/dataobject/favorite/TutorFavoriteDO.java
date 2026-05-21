package cn.iocoder.yudao.module.tutor.dal.dataobject.favorite;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.tutor.enums.target.TutorTargetTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("tutor_favorite")
@KeySequence("tutor_favorite_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorFavoriteDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    /**
     * 枚举 {@link TutorTargetTypeEnum}
     */
    private String targetType;
    private Long targetId;
    private Long targetUserId;
    private String title;
    private String cityCode;
    private String cityName;

}
