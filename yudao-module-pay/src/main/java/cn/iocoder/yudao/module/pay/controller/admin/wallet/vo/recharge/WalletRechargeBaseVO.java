package cn.iocoder.yudao.module.pay.controller.admin.wallet.vo.recharge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 钱包充值 Base VO")
@Data
public class WalletRechargeBaseVO {

    @Schema(description = "钱包编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long walletId;

    @Schema(description = "实际支付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer payPrice;

    @Schema(description = "赠送金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    private Integer bonusPrice;

    @Schema(description = "到账总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "120")
    private Integer totalPrice;

    @Schema(description = "充值套餐编号", example = "1")
    private Long packageId;

    @Schema(description = "是否已支付", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean payStatus;

    @Schema(description = "支付订单编号", example = "1024")
    private Long payOrderId;

    @Schema(description = "支付渠道", example = "wx_lite")
    private String payChannelCode;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

}
