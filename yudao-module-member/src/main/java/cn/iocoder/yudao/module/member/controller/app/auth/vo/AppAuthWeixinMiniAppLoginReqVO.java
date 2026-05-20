package cn.iocoder.yudao.module.member.controller.app.auth.vo;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Schema(description = "用户 APP - 微信小程序手机登录 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppAuthWeixinMiniAppLoginReqVO {

    @Schema(description = "手机 code，小程序通过 wx.getPhoneNumber 方法获得", requiredMode = Schema.RequiredMode.REQUIRED, example = "hello")
    @NotEmpty(message = "手机 code 不能为空")
    private String phoneCode;

    @Schema(description = "登录 code，小程序通过 wx.login 方法获得", requiredMode = Schema.RequiredMode.REQUIRED, example = "word")
    @NotEmpty(message = "登录 code 不能为空")
    private String loginCode;

    @Schema(description = "state", requiredMode = Schema.RequiredMode.REQUIRED, example = "9b2ffbc1-7425-4155-9894-9d5c08541d62")
    @NotEmpty(message = "state 不能为空")
    private String state;

    @Schema(description = "家教身份：1 家长，2 教师", example = "1")
    @Min(value = 1, message = "家教身份不正确")
    @Max(value = 2, message = "家教身份不正确")
    private Integer tutorRole;

    @Schema(description = "家教服务城市编码", example = "110100")
    private String tutorCityCode;

    @AssertTrue(message = "家教服务城市不能为空")
    public boolean isTutorCityCodeValid() {
        return tutorRole == null || StrUtil.isNotEmpty(tutorCityCode);
    }

}
