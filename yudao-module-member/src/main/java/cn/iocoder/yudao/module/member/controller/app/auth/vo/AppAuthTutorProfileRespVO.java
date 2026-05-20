package cn.iocoder.yudao.module.member.controller.app.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "用户 APP - 登录返回的家教档案 Response VO")
@Data
@Builder
public class AppAuthTutorProfileRespVO {

    @Schema(description = "档案编号", example = "1")
    private Long id;

    @Schema(description = "身份：1 家长，2 教师", example = "1")
    private Integer role;

    @Schema(description = "身份名称", example = "家长")
    private String roleName;

    @Schema(description = "城市编号", example = "1")
    private Long cityId;

    @Schema(description = "城市编码", example = "110100")
    private String cityCode;

    @Schema(description = "城市名称", example = "北京")
    private String cityName;

}
