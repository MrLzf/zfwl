package cn.iocoder.yudao.module.tutor.controller.app.complaint.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AppTutorComplaintCreateReqVO {
    @NotBlank(message = "举报目标类型不能为空")
    private String targetType;
    @NotNull(message = "举报目标编号不能为空")
    private Long targetId;
    @NotBlank(message = "举报原因不能为空")
    private String reasonType;
    @Size(max = 1000, message = "举报说明不能超过 1000 个字符")
    private String content;
    private String imageUrls;
}
