package cn.iocoder.yudao.module.tutor.controller.app.message.vo.chat;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AppTutorChatMessageSendReqVO {
    @NotNull(message = "接收人不能为空")
    private Long receiverUserId;
    @NotBlank(message = "消息类型不能为空")
    private String messageType;
    private String content;
    private String imageUrl;
}
