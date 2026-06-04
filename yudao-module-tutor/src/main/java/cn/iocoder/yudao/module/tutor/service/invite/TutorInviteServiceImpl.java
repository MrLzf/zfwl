package cn.iocoder.yudao.module.tutor.service.invite;

import cn.hutool.core.util.RandomUtil;
import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.tutor.dal.dataobject.invite.TutorInviteRecordDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.invite.TutorInviteUserDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.invite.TutorInviteRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.invite.TutorInviteUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.INVITE_CODE_NOT_EXISTS;

@Service
@Validated
public class TutorInviteServiceImpl implements TutorInviteService {

    private static final int LINK_VALID_DAYS = 30;
    private static final int MONTHLY_REWARD_LIMIT = 100;
    private static final int INVITE_REWARD_POINT = 10;

    @Resource
    private TutorInviteUserMapper inviteUserMapper;
    @Resource
    private TutorInviteRecordMapper inviteRecordMapper;
    @Resource
    private MemberPointApi memberPointApi;

    @Override
    public TutorInviteUserDO getOrCreateInvite(Long userId) {
        TutorInviteUserDO invite = inviteUserMapper.selectByUserId(userId);
        if (invite != null && invite.getLinkExpireTime() != null
                && invite.getLinkExpireTime().isAfter(LocalDateTime.now())) {
            return invite;
        }
        String code = invite == null ? generateInviteCode() : invite.getInviteCode();
        TutorInviteUserDO saveObj = TutorInviteUserDO.builder()
                .id(invite == null ? null : invite.getId())
                .userId(userId)
                .inviteCode(code)
                .inviteLink("/pages/tutor/invite/register?code=" + code)
                .linkExpireTime(LocalDateTime.now().plusDays(LINK_VALID_DAYS))
                .build();
        if (invite == null) {
            inviteUserMapper.insert(saveObj);
        } else {
            inviteUserMapper.updateById(saveObj);
        }
        return saveObj;
    }

    @Override
    @Transactional
    public boolean rewardInvite(String inviteCode, Long inviteeUserId, String deviceId, String ip) {
        TutorInviteUserDO inviter = inviteUserMapper.selectByInviteCode(inviteCode);
        if (inviter == null || Objects.equals(inviter.getUserId(), inviteeUserId)) {
            throw exception(INVITE_CODE_NOT_EXISTS);
        }
        if (inviteRecordMapper.selectCountByDeviceOrIp(deviceId, ip) > 0) {
            return false;
        }
        LocalDate now = LocalDate.now();
        LocalDateTime begin = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = begin.plusMonths(1);
        if (inviteRecordMapper.selectMonthlyCount(inviter.getUserId(), begin, end) >= MONTHLY_REWARD_LIMIT) {
            return false;
        }
        TutorInviteRecordDO record = TutorInviteRecordDO.builder()
                .inviterUserId(inviter.getUserId())
                .inviteeUserId(inviteeUserId)
                .inviteCode(inviteCode)
                .deviceId(deviceId)
                .ip(ip)
                .rewardPoint(INVITE_REWARD_POINT)
                .build();
        inviteRecordMapper.insert(record);
        memberPointApi.addPoint(inviter.getUserId(), INVITE_REWARD_POINT,
                MemberPointBizTypeEnum.TUTOR_INVITE_REWARD.getType(), "invite:" + inviteeUserId);
        return true;
    }

    private String generateInviteCode() {
        return RandomUtil.randomString("ABCDEFGHJKLMNPQRSTUVWXYZ23456789", 8).toUpperCase(Locale.ROOT);
    }
}
