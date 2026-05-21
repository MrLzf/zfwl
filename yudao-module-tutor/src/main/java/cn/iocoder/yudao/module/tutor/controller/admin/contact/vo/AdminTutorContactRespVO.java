package cn.iocoder.yudao.module.tutor.controller.admin.contact.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 家教联系记录 Response VO")
@Data
@Builder
public class AdminTutorContactRespVO {

    private Long id;
    private Long viewerUserId;
    private String viewerNickname;
    private String viewerMobile;
    private String targetType;
    private Long targetId;
    private Long targetUserId;
    private String targetNickname;
    private String targetMobile;
    private Integer pointCost;
    private LocalDateTime freeReuseUntil;
    private Boolean viewedMobile;
    private Boolean viewedWechat;
    private LocalDateTime createTime;

}
