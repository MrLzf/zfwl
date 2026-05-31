package cn.iocoder.yudao.module.tutor.controller.app.point.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "用户 App - 家教积分任务 Response VO")
@Data
@Builder
public class AppTutorPointTaskRespVO {

    private String type;
    private String title;
    private String description;
    private Integer point;
    private Boolean completed;
    private String action;
    private String path;

}
