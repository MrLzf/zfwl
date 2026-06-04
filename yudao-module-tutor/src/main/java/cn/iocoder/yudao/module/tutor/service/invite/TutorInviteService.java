package cn.iocoder.yudao.module.tutor.service.invite;

import cn.iocoder.yudao.module.tutor.dal.dataobject.invite.TutorInviteUserDO;

public interface TutorInviteService {

    TutorInviteUserDO getOrCreateInvite(Long userId);

    boolean rewardInvite(String inviteCode, Long inviteeUserId, String deviceId, String ip);
}
