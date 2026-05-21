package cn.iocoder.yudao.module.tutor.dal.dataobject.review;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("tutor_review")
@KeySequence("tutor_review_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorReviewDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long matchId;
    private Long reviewerUserId;
    private Long targetUserId;
    private Integer rating;
    private String tags;
    private String content;
    private Boolean anonymousDisplay;
    /**
     * 0 正常，1 隐藏。
     */
    private Integer status;

}
