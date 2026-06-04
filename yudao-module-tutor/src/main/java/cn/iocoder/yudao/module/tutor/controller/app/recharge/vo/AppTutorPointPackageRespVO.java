package cn.iocoder.yudao.module.tutor.controller.app.recharge.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppTutorPointPackageRespVO {
    private Long id;
    private String name;
    private Integer point;
    private Integer bonusPoint;
    private Integer totalPoint;
    private Integer price;
}
