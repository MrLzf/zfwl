package cn.iocoder.yudao.module.tutor.service.message;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.dal.dataobject.notify.NotifyMessageDO;
import cn.iocoder.yudao.module.system.service.notify.NotifyMessageService;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessagePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessageRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessageSummaryRespVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TutorMessageServiceImpl implements TutorMessageService {

    private static final List<String> CATEGORIES = Arrays.asList("audit", "contact", "match", "review", "point");

    @Resource
    private NotifyMessageService notifyMessageService;

    @Override
    public AppTutorMessageSummaryRespVO getSummary(Long userId) {
        List<NotifyMessageDO> messages = getMessages(userId);
        List<AppTutorMessageSummaryRespVO.CategorySummary> categories = new ArrayList<>();
        for (String category : CATEGORIES) {
            AppTutorMessageSummaryRespVO.CategorySummary summary = new AppTutorMessageSummaryRespVO.CategorySummary();
            summary.setCategory(category);
            List<NotifyMessageDO> categoryMessages = filter(messages, category);
            summary.setUnreadCount(categoryMessages.stream()
                    .filter(message -> !Boolean.TRUE.equals(message.getReadStatus())).count());
            if (!categoryMessages.isEmpty()) {
                summary.setLatestContent(categoryMessages.get(0).getTemplateContent());
                summary.setLatestTime(categoryMessages.get(0).getCreateTime());
            }
            categories.add(summary);
        }
        AppTutorMessageSummaryRespVO result = new AppTutorMessageSummaryRespVO();
        result.setCategories(categories);
        result.setTotalUnread(categories.stream().mapToLong(AppTutorMessageSummaryRespVO.CategorySummary::getUnreadCount).sum());
        return result;
    }

    @Override
    public PageResult<AppTutorMessageRespVO> getPage(Long userId, AppTutorMessagePageReqVO pageReqVO) {
        PageResult<NotifyMessageDO> page = notifyMessageService.getTutorNotifyMessagePage(
                pageReqVO, userId, UserTypeEnum.MEMBER.getValue(), pageReqVO.getCategory());
        List<AppTutorMessageRespVO> list = page.getList().stream()
                .map(this::convert).collect(Collectors.toList());
        return new PageResult<>(list, page.getTotal());
    }

    @Override
    public void read(Long userId, Long id) {
        notifyMessageService.updateTutorNotifyMessageRead(id, userId, UserTypeEnum.MEMBER.getValue());
    }

    @Override
    public void readAll(Long userId, String category) {
        notifyMessageService.updateAllTutorNotifyMessageRead(userId, UserTypeEnum.MEMBER.getValue(), category);
    }

    private List<NotifyMessageDO> getMessages(Long userId) {
        return notifyMessageService.getTutorNotifyMessageList(userId, UserTypeEnum.MEMBER.getValue());
    }

    private List<NotifyMessageDO> filter(List<NotifyMessageDO> messages, String category) {
        return messages.stream().filter(message -> category.equals(param(message, "category")))
                .collect(Collectors.toList());
    }

    private AppTutorMessageRespVO convert(NotifyMessageDO message) {
        AppTutorMessageRespVO vo = new AppTutorMessageRespVO();
        vo.setId(message.getId());
        vo.setCategory(stringParam(message, "category"));
        String title = stringParam(message, "title");
        vo.setTitle(title == null ? message.getTemplateNickname() : title);
        vo.setContent(message.getTemplateContent());
        vo.setReadStatus(message.getReadStatus());
        vo.setCreateTime(message.getCreateTime());
        vo.setAction(stringParam(message, "action"));
        vo.setBizId(stringParam(message, "bizId"));
        vo.setTargetType(stringParam(message, "targetType"));
        Object targetId = param(message, "targetId");
        if (targetId instanceof Number) {
            vo.setTargetId(((Number) targetId).longValue());
        }
        return vo;
    }

    private String stringParam(NotifyMessageDO message, String key) {
        Object value = param(message, key);
        return value == null ? null : String.valueOf(value);
    }

    private Object param(NotifyMessageDO message, String key) {
        Map<String, Object> params = message.getTemplateParams();
        return params == null ? null : params.get(key);
    }

}
