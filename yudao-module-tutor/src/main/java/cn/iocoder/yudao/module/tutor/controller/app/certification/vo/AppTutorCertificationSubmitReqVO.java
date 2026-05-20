package cn.iocoder.yudao.module.tutor.controller.app.certification.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Schema(description = "用户 App - 教师认证提交 Request VO")
@Data
public class AppTutorCertificationSubmitReqVO {

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @Schema(description = "身份证号", requiredMode = Schema.RequiredMode.REQUIRED, example = "110101199001011234")
    @NotBlank(message = "身份证号不能为空")
    private String idCardNo;

    @Schema(description = "学历证明 URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "学历证明不能为空")
    private String educationFileUrl;

    @Schema(description = "教师资格证 URL")
    private String teacherCertFileUrl;

}
