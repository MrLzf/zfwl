package cn.iocoder.yudao.module.tutor.controller.app.subscribe.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppTutorSubscribeSettingRespVO {
    private String noticeType;
    private Boolean enabled;
}
