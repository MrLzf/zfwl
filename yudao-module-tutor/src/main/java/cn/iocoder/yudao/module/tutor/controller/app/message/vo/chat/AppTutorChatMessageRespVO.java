package cn.iocoder.yudao.module.tutor.controller.app.message.vo.chat;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppTutorChatMessageRespVO {
    private Long id;
    private Long userId;
    private Long receiverUserId;
    private String messageType;
    private String content;
    private String imageUrl;
    private Boolean readStatus;
    private LocalDateTime createTime;
}
