package cn.iocoder.yudao.module.tutor.controller.app.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppTutorMessageRespVO {

    private Long id;
    private String category;
    private String title;
    private String content;
    private Boolean readStatus;
    private LocalDateTime createTime;
    private String action;
    private String bizId;
    private String targetType;
    private Long targetId;

}
