package cn.iocoder.yudao.module.tutor.controller.admin.review.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 家教评价状态更新 Request VO")
@Data
public class AdminTutorReviewUpdateStatusReqVO {

    @NotNull(message = "评价编号不能为空")
    private Long id;

    @NotNull(message = "状态不能为空")
    private Integer status;

}
