package cn.iocoder.yudao.module.tutor.controller.app.match.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 App - 匹配记录 Response VO")
@Data
@Builder
public class AppTutorMatchRespVO {
    private Long id;
    private Long demandId;
    private Long resumeId;
    private Long parentUserId;
    private Long teacherUserId;
    private String source;
    private Integer status;
    private String statusName;
    private LocalDateTime parentConfirmTime;
    private LocalDateTime teacherConfirmTime;
    private LocalDateTime matchedTime;
    private LocalDateTime createTime;
}
