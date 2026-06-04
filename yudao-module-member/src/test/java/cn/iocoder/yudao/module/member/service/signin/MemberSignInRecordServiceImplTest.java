package cn.iocoder.yudao.module.member.service.signin;

import cn.iocoder.yudao.module.member.dal.dataobject.signin.MemberSignInConfigDO;
import cn.iocoder.yudao.module.member.dal.dataobject.signin.MemberSignInRecordDO;
import cn.iocoder.yudao.module.member.dal.mysql.signin.MemberSignInRecordMapper;
import cn.iocoder.yudao.module.member.service.level.MemberLevelService;
import cn.iocoder.yudao.module.member.service.point.MemberPointRecordService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.system.api.notify.NotifyMessageSendApi;
import cn.iocoder.yudao.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.ArrayList;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.SIGN_IN_CONFIG_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberSignInRecordServiceImplTest {

    @InjectMocks
    private MemberSignInRecordServiceImpl signInRecordService;

    @Mock
    private MemberSignInRecordMapper signInRecordMapper;
    @Mock
    private MemberSignInConfigService signInConfigService;
    @Mock
    private MemberPointRecordService pointRecordService;
    @Mock
    private MemberLevelService memberLevelService;
    @Mock
    private MemberUserService memberUserService;
    @Mock
    private NotifyMessageSendApi notifyMessageSendApi;

    @Test
    void testCreateSignRecord_noEnabledConfig() {
        Long userId = 6L;
        when(signInConfigService.getSignInConfigList(0)).thenReturn(Collections.emptyList());

        assertServiceException(() -> signInRecordService.createSignRecord(userId), SIGN_IN_CONFIG_NOT_EXISTS);
    }

    @Test
    void testCreateSignRecord_missingFirstDayConfig() {
        Long userId = 6L;
        MemberSignInConfigDO config = new MemberSignInConfigDO().setDay(7).setPoint(5).setExperience(10);
        when(signInConfigService.getSignInConfigList(0)).thenReturn(new ArrayList<>(Collections.singletonList(config)));

        assertServiceException(() -> signInRecordService.createSignRecord(userId), SIGN_IN_CONFIG_NOT_EXISTS);
    }

    @Test
    void testCreateSignRecord_withPoint_sendsPointNotification() {
        Long userId = 6L;
        MemberSignInConfigDO config = new MemberSignInConfigDO().setDay(1).setPoint(5).setExperience(10);
        when(signInConfigService.getSignInConfigList(0)).thenReturn(new ArrayList<>(Collections.singletonList(config)));
        doAnswer(invocation -> {
            invocation.getArgument(0, MemberSignInRecordDO.class)
                    .setId(99L);
            return null;
        }).when(signInRecordMapper).insert(any(MemberSignInRecordDO.class));

        signInRecordService.createSignRecord(userId);

        verify(pointRecordService).createPointRecord(userId, 5,
                cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum.SIGN, "99");
        ArgumentCaptor<NotifySendSingleToUserReqDTO> captor = ArgumentCaptor.forClass(NotifySendSingleToUserReqDTO.class);
        verify(notifyMessageSendApi).sendSingleMessageToMember(captor.capture());
        NotifySendSingleToUserReqDTO request = captor.getValue();
        assertEquals(userId, request.getUserId());
        assertEquals("tutor_point_changed", request.getTemplateCode());
        assertEquals("每日签到", request.getTemplateParams().get("title"));
        assertEquals(5, request.getTemplateParams().get("point"));
        assertEquals("point", request.getTemplateParams().get("category"));
        assertEquals("point_records", request.getTemplateParams().get("action"));
        assertEquals("sign:99", request.getTemplateParams().get("bizId"));
        assertEquals("sign_in", request.getTemplateParams().get("targetType"));
        assertEquals(99L, request.getTemplateParams().get("targetId"));
    }

}
