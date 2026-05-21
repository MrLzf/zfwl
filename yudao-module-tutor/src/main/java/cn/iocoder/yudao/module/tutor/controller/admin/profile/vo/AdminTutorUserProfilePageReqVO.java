package cn.iocoder.yudao.module.tutor.controller.admin.profile.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 家教用户档案分页 Request VO")
@Data
public class AdminTutorUserProfilePageReqVO extends PageParam {

    @Schema(description = "会员用户编号", example = "1024")
    private Long userId;

    @Schema(description = "身份：1 家长，2 教师", example = "1")
    private Integer role;

    @Schema(description = "城市编码", example = "110100")
    private String cityCode;

    @Schema(description = "教师认证状态", example = "20")
    private Integer certificationStatus;

    @Schema(description = "会员手机号", example = "13800138000")
    private String mobile;

    @Schema(description = "会员昵称", example = "小王")
    private String nickname;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
