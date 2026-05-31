package cn.iocoder.yudao.module.tutor.service.message;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.system.dal.dataobject.notify.NotifyMessageDO;
import cn.iocoder.yudao.module.system.service.notify.NotifyMessageService;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessagePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessageRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessageSummaryRespVO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TutorMessageServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorMessageServiceImpl tutorMessageService;

    @Mock
    private NotifyMessageService notifyMessageService;

    @Test
    void getSummary_returnsLatestAndUnreadForThreeCategories() {
        Long userId = 10L;
        when(notifyMessageService.getTutorNotifyMessageList(userId, UserTypeEnum.MEMBER.getValue()))
                .thenReturn(Arrays.asList(
                        message(4L, "audit", "审核新消息", false, 4),
                        message(3L, "point", "积分消息", false, 3),
                        message(2L, "audit", "审核旧消息", true, 2),
                        message(1L, "contact", "联系消息", false, 1)));

        AppTutorMessageSummaryRespVO summary = tutorMessageService.getSummary(userId);

        assertEquals(3, summary.getTotalUnread());
        assertCategory(summary, "audit", "审核新消息", 1);
        assertCategory(summary, "contact", "联系消息", 1);
        assertCategory(summary, "point", "积分消息", 1);
    }

    @Test
    void getPage_returnsOnlyRequestedCategoryAndMapsActionFields() {
        Long userId = 10L;
        NotifyMessageDO audit = message(3L, "audit", "审核消息", false, 3);
        audit.getTemplateParams().put("action", "my_posts");
        audit.getTemplateParams().put("bizId", "resume:20");
        audit.getTemplateParams().put("targetType", "resume");
        audit.getTemplateParams().put("targetId", 20L);
        AppTutorMessagePageReqVO reqVO = new AppTutorMessagePageReqVO();
        reqVO.setCategory("audit");
        when(notifyMessageService.getTutorNotifyMessagePage(reqVO, userId, UserTypeEnum.MEMBER.getValue(), "audit"))
                .thenReturn(new PageResult<>(Collections.singletonList(audit), 1L));

        PageResult<AppTutorMessageRespVO> page = tutorMessageService.getPage(userId, reqVO);

        assertEquals(1, page.getTotal());
        AppTutorMessageRespVO item = page.getList().get(0);
        assertEquals("审核消息", item.getContent());
        assertEquals("my_posts", item.getAction());
        assertEquals("resume:20", item.getBizId());
        assertEquals("resume", item.getTargetType());
        assertEquals(20L, item.getTargetId());
    }

    @Test
    void read_delegatesOwnershipScopedUpdate() {
        tutorMessageService.read(10L, 20L);

        verify(notifyMessageService).updateTutorNotifyMessageRead(20L, 10L, UserTypeEnum.MEMBER.getValue());
    }

    @Test
    void readAll_withCategoryDelegatesCategoryUpdate() {
        tutorMessageService.readAll(10L, "contact");

        verify(notifyMessageService).updateAllTutorNotifyMessageRead(10L, UserTypeEnum.MEMBER.getValue(), "contact");
    }

    @Test
    void readAll_withoutCategoryDelegatesAllTutorUpdate() {
        tutorMessageService.readAll(10L, null);

        verify(notifyMessageService).updateAllTutorNotifyMessageRead(10L, UserTypeEnum.MEMBER.getValue(), null);
    }

    private void assertCategory(AppTutorMessageSummaryRespVO summary, String category, String content, int unread) {
        AppTutorMessageSummaryRespVO.CategorySummary item = summary.getCategories().stream()
                .filter(categorySummary -> category.equals(categorySummary.getCategory()))
                .findFirst().orElseThrow(AssertionError::new);
        assertEquals(content, item.getLatestContent());
        assertEquals(unread, item.getUnreadCount());
    }

    private NotifyMessageDO message(Long id, String category, String content, boolean readStatus, int minute) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        NotifyMessageDO message = new NotifyMessageDO().setId(id).setTemplateCode("tutor_test").setTemplateNickname("家教助手")
                .setTemplateContent(content).setTemplateParams(params).setReadStatus(readStatus);
        message.setCreateTime(LocalDateTime.of(2026, 5, 31, 10, minute));
        return message;
    }

}
