package cn.iocoder.yudao.module.tutor.controller.admin.resume.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 教师简历分页 Request VO")
@Data
public class AdminTutorTeacherResumePageReqVO extends PageParam {

    @Schema(description = "用户编号", example = "1024")
    private Long userId;

    @Schema(description = "标题", example = "985硕士")
    private String title;

    @Schema(description = "城市编码", example = "110100")
    private String cityCode;

    @Schema(description = "科目", example = "数学")
    private String subject;

    @Schema(description = "授课模式", example = "1")
    private Integer teachMode;

    @Schema(description = "是否支持试课", example = "true")
    private Boolean freeTrialEnabled;

    @Schema(description = "发布状态", example = "10")
    private Integer status;

    @Schema(description = "审核状态", example = "10")
    private Integer auditStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
