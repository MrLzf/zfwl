package cn.iocoder.yudao.module.tutor.service.invite;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.tutor.dal.dataobject.invite.TutorInviteRecordDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.invite.TutorInviteUserDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.invite.TutorInviteRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.invite.TutorInviteUserMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TutorInviteServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorInviteServiceImpl inviteService;

    @Mock
    private TutorInviteUserMapper inviteUserMapper;
    @Mock
    private TutorInviteRecordMapper inviteRecordMapper;
    @Mock
    private MemberPointApi memberPointApi;

    @Test
    void getOrCreateInvite_createsUniqueCodeAndThirtyDayLink() {
        when(inviteUserMapper.selectByUserId(100L)).thenReturn(null);

        TutorInviteUserDO invite = inviteService.getOrCreateInvite(100L);

        assertNotNull(invite.getInviteCode());
        assertTrue(invite.getLinkExpireTime().isAfter(java.time.LocalDateTime.now().plusDays(29)));
        verify(inviteUserMapper).insert(any(TutorInviteUserDO.class));
    }

    @Test
    void rewardInvite_skipsSameDeviceOrSameIpAndMonthlyLimit() {
        TutorInviteUserDO inviter = TutorInviteUserDO.builder().userId(100L).inviteCode("ABC123").build();
        when(inviteUserMapper.selectByInviteCode("ABC123")).thenReturn(inviter);
        when(inviteRecordMapper.selectCountByDeviceOrIp("dev-1", "127.0.0.1")).thenReturn(1L);

        boolean rewarded = inviteService.rewardInvite("ABC123", 200L, "dev-1", "127.0.0.1");

        assertFalse(rewarded);
        verify(memberPointApi, never()).addPoint(anyLong(), anyInt(), anyInt(), anyString());
        verify(inviteRecordMapper, never()).insert(any(TutorInviteRecordDO.class));
    }
}
