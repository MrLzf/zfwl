package cn.iocoder.yudao.module.tutor.controller.admin.complaint.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AdminTutorComplaintHandleReqVO {
    @NotNull(message = "举报编号不能为空")
    private Long id;
    @NotNull(message = "处理状态不能为空")
    private Integer status;
    @NotBlank(message = "处理结果不能为空")
    private String handleResult;
}
