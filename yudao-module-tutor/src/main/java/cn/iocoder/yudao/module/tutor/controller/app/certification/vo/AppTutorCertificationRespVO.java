package cn.iocoder.yudao.module.tutor.controller.app.certification.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 App - 教师认证 Response VO")
@Data
@Builder
public class AppTutorCertificationRespVO {

    private Long id;
    private Long userId;
    private Long teacherProfileId;
    private String realName;
    private String idCardNoMask;
    private String educationFileUrl;
    private String teacherCertFileUrl;
    private Integer status;
    private String statusName;
    private String rejectReason;
    private Long auditorId;
    private LocalDateTime auditTime;
    private LocalDateTime createTime;

}
