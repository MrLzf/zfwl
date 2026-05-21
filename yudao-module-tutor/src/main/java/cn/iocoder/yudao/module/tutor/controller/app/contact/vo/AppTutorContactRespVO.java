package cn.iocoder.yudao.module.tutor.controller.app.contact.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "用户 App - 查看联系方式 Response VO")
@Data
@Builder
public class AppTutorContactRespVO {
    private String targetType;
    private Long targetId;
    private Long targetUserId;
    private String mobile;
    private String wechat;
    private Integer pointCost;
    private Boolean reused;
    private String safetyTip;
}
