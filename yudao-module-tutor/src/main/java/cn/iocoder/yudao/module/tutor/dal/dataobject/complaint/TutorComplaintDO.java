package cn.iocoder.yudao.module.tutor.dal.dataobject.complaint;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("tutor_complaint")
@KeySequence("tutor_complaint_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorComplaintDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String targetType;
    private Long targetId;
    private Long targetUserId;
    private String reasonType;
    private String content;
    private String imageUrls;
    /**
     * 0 待处理，10 已处理，20 已驳回。
     */
    private Integer status;
    private Long handlerId;
    private String handleResult;
    private LocalDateTime handleTime;
}
