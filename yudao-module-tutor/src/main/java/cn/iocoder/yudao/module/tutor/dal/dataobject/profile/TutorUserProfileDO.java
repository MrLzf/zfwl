package cn.iocoder.yudao.module.tutor.dal.dataobject.profile;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 家教用户档案 DO。
 */
@TableName("tutor_user_profile")
@KeySequence("tutor_user_profile_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorUserProfileDO extends TenantBaseDO {

    /**
     * 编号。
     */
    @TableId
    private Long id;
    /**
     * 会员用户编号。
     */
    private Long userId;
    /**
     * 用户身份。
     *
     * 枚举 {@link TutorUserRoleEnum}
     */
    private Integer role;
    /**
     * 当前城市编号。
     */
    private Long cityId;
    /**
     * 当前城市编码。
     */
    private String cityCode;
    /**
     * 当前城市名称。
     */
    private String cityName;
    /**
     * 当前定位经度。
     */
    private BigDecimal longitude;
    /**
     * 当前定位纬度。
     */
    private BigDecimal latitude;
    /**
     * 当前定位地址。
     */
    private String locationAddress;
    /**
     * 最近定位时间。
     */
    private LocalDateTime locationTime;
    /**
     * 状态。
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
