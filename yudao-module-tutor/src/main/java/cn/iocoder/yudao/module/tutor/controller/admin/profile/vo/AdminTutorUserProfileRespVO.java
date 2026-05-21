package cn.iocoder.yudao.module.tutor.controller.admin.profile.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 家教用户档案 Response VO")
@Data
@Builder
public class AdminTutorUserProfileRespVO {

    private Long id;
    private Long userId;
    private String nickname;
    private String mobile;
    private Integer point;
    private Integer role;
    private String cityCode;
    private String cityName;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String locationAddress;
    private LocalDateTime locationTime;
    private Integer status;
    private Integer certificationStatus;
    private LocalDateTime createTime;

}
