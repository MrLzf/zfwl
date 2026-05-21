package cn.iocoder.yudao.module.tutor.dal.dataobject.match;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.tutor.enums.match.TutorMatchStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("tutor_match_record")
@KeySequence("tutor_match_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorMatchRecordDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long demandId;
    private Long resumeId;
    private Long parentUserId;
    private Long teacherUserId;
    private String source;
    /**
     * 枚举 {@link TutorMatchStatusEnum}
     */
    private Integer status;
    private LocalDateTime parentConfirmTime;
    private LocalDateTime teacherConfirmTime;
    private LocalDateTime matchedTime;

}
