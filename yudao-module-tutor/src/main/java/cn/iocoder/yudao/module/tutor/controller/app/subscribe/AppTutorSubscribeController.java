package cn.iocoder.yudao.module.tutor.controller.app.subscribe;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.subscribe.vo.AppTutorSubscribeSettingReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.subscribe.vo.AppTutorSubscribeSettingRespVO;
import cn.iocoder.yudao.module.tutor.service.subscribe.TutorSubscribeMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 家教微信订阅消息")
@RestController
@RequestMapping("/tutor/subscribe-messages")
@Validated
public class AppTutorSubscribeController {

    @Resource
    private TutorSubscribeMessageService subscribeMessageService;

    @GetMapping("/settings")
    @Operation(summary = "获得订阅消息设置")
    public CommonResult<List<AppTutorSubscribeSettingRespVO>> getSettings() {
        return success(subscribeMessageService.getSettings(getLoginUserId()).stream()
                .map(setting -> AppTutorSubscribeSettingRespVO.builder()
                        .noticeType(setting.getNoticeType()).enabled(setting.getEnabled()).build())
                .collect(Collectors.toList()));
    }

    @PutMapping("/settings")
    @Operation(summary = "更新订阅消息设置")
    public CommonResult<Boolean> updateSetting(@RequestBody @Valid AppTutorSubscribeSettingReqVO reqVO) {
        subscribeMessageService.updateSetting(getLoginUserId(), reqVO);
        return success(Boolean.TRUE);
    }
}
