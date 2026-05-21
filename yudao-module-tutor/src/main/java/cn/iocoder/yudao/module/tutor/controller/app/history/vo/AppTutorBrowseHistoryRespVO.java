package cn.iocoder.yudao.module.tutor.controller.app.history.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 App - 浏览历史 Response VO")
@Data
@Builder
public class AppTutorBrowseHistoryRespVO {

    private Long id;
    private String targetType;
    private Long targetId;
    private Long targetUserId;
    private String title;
    private String cityCode;
    private String cityName;
    private LocalDateTime createTime;

}
