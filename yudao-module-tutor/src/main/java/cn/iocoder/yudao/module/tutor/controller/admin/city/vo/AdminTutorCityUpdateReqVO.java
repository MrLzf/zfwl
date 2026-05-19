package cn.iocoder.yudao.module.tutor.controller.admin.city.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 家教城市更新 Request VO")
@Data
public class AdminTutorCityUpdateReqVO {

    @Schema(description = "城市编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "城市编号不能为空")
    private Long id;

    @Schema(description = "是否开通", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否开通不能为空")
    private Boolean opened;

    @Schema(description = "是否热门城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否热门城市不能为空")
    private Boolean hot;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

}
