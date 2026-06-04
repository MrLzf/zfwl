package cn.iocoder.yudao.module.tutor.controller.app.recharge.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppTutorPointRechargeOrderRespVO {
    private Long id;
    private Long payOrderId;
    private String merchantOrderId;
    private Integer totalPoint;
    private Integer price;
    private Integer status;
    private LocalDateTime expireTime;
}
