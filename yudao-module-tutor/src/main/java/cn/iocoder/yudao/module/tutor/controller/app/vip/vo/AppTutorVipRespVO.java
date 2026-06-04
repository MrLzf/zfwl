package cn.iocoder.yudao.module.tutor.controller.app.vip.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppTutorVipRespVO {
    private Boolean active;
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer monthlyGiftPoint;
}
