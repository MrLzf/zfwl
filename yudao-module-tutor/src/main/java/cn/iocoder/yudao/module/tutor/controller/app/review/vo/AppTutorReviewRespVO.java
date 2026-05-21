package cn.iocoder.yudao.module.tutor.controller.app.review.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 App - 家教评价 Response VO")
@Data
@Builder
public class AppTutorReviewRespVO {
    private Long id;
    private Long matchId;
    private Long reviewerUserId;
    private Long targetUserId;
    private Integer rating;
    private String tags;
    private String content;
    private Boolean anonymousDisplay;
    private Integer status;
    private LocalDateTime createTime;
}
