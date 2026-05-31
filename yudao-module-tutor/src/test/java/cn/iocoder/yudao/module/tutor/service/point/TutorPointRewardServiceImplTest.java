package cn.iocoder.yudao.module.tutor.service.point;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.tutor.dal.dataobject.point.TutorPointRewardRecordDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.point.TutorPointRewardRecordMapper;
import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TutorPointRewardServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorPointRewardServiceImpl rewardService;

    @Mock
    private TutorPointRewardRecordMapper rewardRecordMapper;
    @Mock
    private MemberPointApi memberPointApi;
    @Mock
    private MemberUserApi memberUserApi;
    @Mock
    private TutorNotifyService tutorNotifyService;

    @Test
    void reward_whenFirstClaim_insertsRecordAndAddsPoint() {
        when(rewardRecordMapper.selectByUniqueKey(100L, "profile_init", "profile_init")).thenReturn(null);
        MemberUserRespDTO user = new MemberUserRespDTO();
        user.setPoint(35);
        when(memberUserApi.getUser(100L)).thenReturn(user);

        boolean rewarded = rewardService.reward(100L, TutorPointTaskTypeEnum.PROFILE_INIT,
                "profile_init", "首次初始化身份档案");

        assertTrue(rewarded);
        verify(rewardRecordMapper).insert(any(TutorPointRewardRecordDO.class));
        verify(memberPointApi).addPoint(100L, 20, MemberPointBizTypeEnum.TUTOR_PROFILE_INIT.getType(),
                "profile_init");
        verify(tutorNotifyService).sendPointChanged(100L, "首次初始化身份档案", 20, 35,
                "point", "point_records", "profile_init", null, null);
    }

    @Test
    void reward_whenAlreadyClaimed_doesNotAddPointAgain() {
        when(rewardRecordMapper.selectByUniqueKey(100L, "profile_init", "profile_init"))
                .thenReturn(TutorPointRewardRecordDO.builder().id(1L).build());

        boolean rewarded = rewardService.reward(100L, TutorPointTaskTypeEnum.PROFILE_INIT,
                "profile_init", "首次初始化身份档案");

        assertFalse(rewarded);
        verify(memberPointApi, never()).addPoint(anyLong(), anyInt(), anyInt(), anyString());
    }

    @Test
    void reward_whenConcurrentClaimHitsUniqueKey_doesNotAddPointAgain() {
        when(rewardRecordMapper.selectByUniqueKey(100L, "profile_init", "profile_init")).thenReturn(null);
        doThrow(new DuplicateKeyException("duplicate")).when(rewardRecordMapper)
                .insert(any(TutorPointRewardRecordDO.class));

        boolean rewarded = rewardService.reward(100L, TutorPointTaskTypeEnum.PROFILE_INIT,
                "profile_init", "首次初始化身份档案");

        assertFalse(rewarded);
        verify(memberPointApi, never()).addPoint(anyLong(), anyInt(), anyInt(), anyString());
    }

}
