package cn.iocoder.yudao.module.tutor.controller.app.review.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;

@Schema(description = "用户 App - 家教评价创建 Request VO")
@Data
public class AppTutorReviewCreateReqVO {
    @Schema(description = "匹配编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "匹配编号不能为空")
    private Long matchId;

    @Schema(description = "评分 1-5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分不能小于 1")
    @Max(value = 5, message = "评分不能大于 5")
    private Integer rating;

    @Schema(description = "标签，多个用逗号分隔")
    private String tags;

    @Schema(description = "评价内容")
    @Size(max = 1000, message = "评价内容不能超过 1000 个字符")
    private String content;

    @Schema(description = "是否匿名展示")
    private Boolean anonymousDisplay;
}
