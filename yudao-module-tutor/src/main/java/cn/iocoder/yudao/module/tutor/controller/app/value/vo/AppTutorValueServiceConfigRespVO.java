package cn.iocoder.yudao.module.tutor.controller.app.value.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppTutorValueServiceConfigRespVO {
    private Long id;
    private String serviceType;
    private String targetType;
    private Integer pointPrice;
    private Integer durationHours;
}
