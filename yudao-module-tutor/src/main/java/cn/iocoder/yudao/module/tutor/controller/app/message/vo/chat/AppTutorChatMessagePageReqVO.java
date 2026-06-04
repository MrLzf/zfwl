package cn.iocoder.yudao.module.tutor.controller.app.message.vo.chat;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppTutorChatMessagePageReqVO extends PageParam {
    @NotNull(message = "对话用户不能为空")
    private Long receiverUserId;
}
