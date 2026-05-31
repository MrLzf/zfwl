package cn.iocoder.yudao.module.tutor.service.notify;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.system.api.notify.NotifyMessageSendApi;
import cn.iocoder.yudao.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TutorNotifyServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorNotifyServiceImpl tutorNotifyService;

    @Mock
    private NotifyMessageSendApi notifyMessageSendApi;

    @Test
    void sendPointChanged_sendsEnrichedParams() {
        tutorNotifyService.sendPointChanged(10L, "查看联系方式", -10, 90,
                "point", "contact_view", "resume:20", "resume", 20L);

        NotifySendSingleToUserReqDTO request = captureSingleRequest();
        assertEquals("tutor_point_changed", request.getTemplateCode());
        assertEquals("查看联系方式", request.getTemplateParams().get("title"));
        assertEquals(-10, request.getTemplateParams().get("point"));
        assertEquals(90, request.getTemplateParams().get("totalPoint"));
        assertEquals("point", request.getTemplateParams().get("category"));
        assertEquals("contact_view", request.getTemplateParams().get("action"));
        assertEquals("resume:20", request.getTemplateParams().get("bizId"));
        assertEquals("resume", request.getTemplateParams().get("targetType"));
        assertEquals(20L, request.getTemplateParams().get("targetId"));
    }

    @Test
    void sendPointChanged_legacyCall_stillProvidesTemplateParams() {
        tutorNotifyService.sendPointChanged(10L, "后台积分调整", 10);

        NotifySendSingleToUserReqDTO request = captureSingleRequest();
        assertEquals("后台积分调整", request.getTemplateParams().get("title"));
        assertEquals("", request.getTemplateParams().get("totalPoint"));
        assertEquals("", request.getTemplateParams().get("category"));
        assertEquals("", request.getTemplateParams().get("action"));
        assertEquals("", request.getTemplateParams().get("bizId"));
        assertEquals("", request.getTemplateParams().get("targetType"));
        assertEquals(null, request.getTemplateParams().get("targetId"));
    }

    @Test
    void sendContactViewed_sendsViewerAndOwnerNotifications() {
        tutorNotifyService.sendContactViewed(10L, 11L, "demand", 20L);

        ArgumentCaptor<NotifySendSingleToUserReqDTO> captor = ArgumentCaptor.forClass(NotifySendSingleToUserReqDTO.class);
        verify(notifyMessageSendApi, times(2)).sendSingleMessageToMember(captor.capture());
        List<NotifySendSingleToUserReqDTO> requests = captor.getAllValues();

        assertContactRequest(requests.get(0), 10L, "tutor_contact_viewer", "contact", "view", 11L, "demand", 20L);
        assertContactRequest(requests.get(1), 11L, "tutor_contact_owner", "contact", "viewed", 10L, "demand", 20L);
    }

    @Test
    void sendContactViewed_whenSendFails_doesNotThrow() {
        doThrow(new IllegalStateException("send failed")).when(notifyMessageSendApi)
                .sendSingleMessageToMember(any(NotifySendSingleToUserReqDTO.class));

        assertDoesNotThrow(() -> tutorNotifyService.sendContactViewed(10L, 11L, "demand", 20L));
    }

    private NotifySendSingleToUserReqDTO captureSingleRequest() {
        ArgumentCaptor<NotifySendSingleToUserReqDTO> captor = ArgumentCaptor.forClass(NotifySendSingleToUserReqDTO.class);
        verify(notifyMessageSendApi).sendSingleMessageToMember(captor.capture());
        return captor.getValue();
    }

    private void assertContactRequest(NotifySendSingleToUserReqDTO request, Long userId, String templateCode,
                                      String category, String action, Long counterpartUserId,
                                      String targetType, Long targetId) {
        assertEquals(userId, request.getUserId());
        assertEquals(templateCode, request.getTemplateCode());
        assertEquals(category, request.getTemplateParams().get("category"));
        assertEquals(action, request.getTemplateParams().get("action"));
        assertEquals(targetType + ":" + targetId, request.getTemplateParams().get("bizId"));
        assertEquals(targetType, request.getTemplateParams().get("targetType"));
        assertEquals(targetId, request.getTemplateParams().get("targetId"));
        assertEquals(counterpartUserId, request.getTemplateParams().get("counterpartUserId"));
    }

}
