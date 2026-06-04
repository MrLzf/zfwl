package cn.iocoder.yudao.module.tutor.controller.app.invite.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppTutorInviteRespVO {
    private String inviteCode;
    private String inviteLink;
    private LocalDateTime linkExpireTime;
}
