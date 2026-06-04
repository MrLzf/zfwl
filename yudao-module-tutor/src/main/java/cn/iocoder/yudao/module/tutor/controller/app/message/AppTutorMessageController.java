package cn.iocoder.yudao.module.tutor.controller.app.message;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessagePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessageRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessageSummaryRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.chat.AppTutorChatMessagePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.chat.AppTutorChatMessageRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.chat.AppTutorChatMessageSendReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.message.TutorChatMessageDO;
import cn.iocoder.yudao.module.tutor.service.message.TutorMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 家教消息")
@RestController
@RequestMapping("/tutor/messages")
@Validated
public class AppTutorMessageController {

    @Resource
    private TutorMessageService tutorMessageService;

    @GetMapping("/summary")
    @Operation(summary = "获得家教消息分类摘要")
    public CommonResult<AppTutorMessageSummaryRespVO> getSummary() {
        return success(tutorMessageService.getSummary(getLoginUserId()));
    }

    @GetMapping("/page")
    @Operation(summary = "获得家教消息分类分页")
    public CommonResult<PageResult<AppTutorMessageRespVO>> getPage(@Valid AppTutorMessagePageReqVO pageReqVO) {
        return success(tutorMessageService.getPage(getLoginUserId(), pageReqVO));
    }

    @PutMapping("/read")
    @Operation(summary = "标记家教消息为已读")
    public CommonResult<Boolean> read(@RequestParam("id") Long id) {
        tutorMessageService.read(getLoginUserId(), id);
        return success(Boolean.TRUE);
    }

    @PutMapping("/read-all")
    @Operation(summary = "标记家教消息为全部已读")
    public CommonResult<Boolean> readAll(
            @RequestParam(value = "category", required = false)
            @Pattern(regexp = "audit|contact|match|review|point", message = "消息分类不正确") String category) {
        tutorMessageService.readAll(getLoginUserId(), category);
        return success(Boolean.TRUE);
    }

    @PostMapping("/chat/send")
    @Operation(summary = "发送客服/留言消息")
    public CommonResult<AppTutorChatMessageRespVO> sendChatMessage(@RequestBody @Valid AppTutorChatMessageSendReqVO reqVO) {
        return success(convert(tutorMessageService.sendChatMessage(getLoginUserId(), reqVO)));
    }

    @GetMapping("/chat/page")
    @Operation(summary = "获得 30 天内客服/留言会话")
    public CommonResult<PageResult<AppTutorChatMessageRespVO>> getChatMessagePage(@Valid AppTutorChatMessagePageReqVO reqVO) {
        PageResult<TutorChatMessageDO> page = tutorMessageService.getChatMessagePage(getLoginUserId(), reqVO);
        return success(new PageResult<>(page.getList().stream().map(AppTutorMessageController::convert)
                .collect(java.util.stream.Collectors.toList()), page.getTotal()));
    }

    private static AppTutorChatMessageRespVO convert(TutorChatMessageDO message) {
        return AppTutorChatMessageRespVO.builder().id(message.getId()).userId(message.getUserId())
                .receiverUserId(message.getReceiverUserId()).messageType(message.getMessageType())
                .content(message.getContent()).imageUrl(message.getImageUrl()).readStatus(message.getReadStatus())
                .createTime(message.getCreateTime()).build();
    }

}
