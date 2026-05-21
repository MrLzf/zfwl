package cn.iocoder.yudao.module.tutor.controller.app.contact.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 App - 联系记录 Response VO")
@Data
@Builder
public class AppTutorContactRecordRespVO {
    private Long id;
    private String targetType;
    private Long targetId;
    private Long targetUserId;
    private Integer pointCost;
    private LocalDateTime freeReuseUntil;
    private LocalDateTime createTime;
}
