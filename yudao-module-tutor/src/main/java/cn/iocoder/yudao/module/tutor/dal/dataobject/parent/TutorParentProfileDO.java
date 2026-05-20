package cn.iocoder.yudao.module.tutor.dal.dataobject.parent;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorTeachModeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 家长资料 DO。
 */
@TableName("tutor_parent_profile")
@KeySequence("tutor_parent_profile_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorParentProfileDO extends TenantBaseDO {

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
     * 家教用户档案编号。
     */
    private Long profileId;
    /**
     * 孩子年级。
     */
    private String childGrade;
    /**
     * 辅导科目，多个用逗号分隔。
     */
    private String subjects;
    /**
     * 每小时最低预算。
     */
    private Integer budgetMin;
    /**
     * 每小时最高预算。
     */
    private Integer budgetMax;
    /**
     * 授课模式偏好。
     *
     * 枚举 {@link TutorTeachModeEnum}
     */
    private Integer teachMode;
    /**
     * 补充说明。
     */
    private String remark;

}
