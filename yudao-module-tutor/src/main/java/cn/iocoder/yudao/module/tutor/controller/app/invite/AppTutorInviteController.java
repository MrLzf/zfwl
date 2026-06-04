package cn.iocoder.yudao.module.tutor.controller.app.invite;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.tutor.controller.app.invite.vo.AppTutorInviteRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.invite.TutorInviteUserDO;
import cn.iocoder.yudao.module.tutor.service.invite.TutorInviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 家教邀请")
@RestController
@RequestMapping("/tutor/invite")
@Validated
public class AppTutorInviteController {

    @Resource
    private TutorInviteService inviteService;

    @GetMapping
    @Operation(summary = "获得我的邀请码和邀请链接")
    public CommonResult<AppTutorInviteRespVO> getInvite() {
        TutorInviteUserDO invite = inviteService.getOrCreateInvite(getLoginUserId());
        return success(AppTutorInviteRespVO.builder().inviteCode(invite.getInviteCode())
                .inviteLink(invite.getInviteLink()).linkExpireTime(invite.getLinkExpireTime()).build());
    }

    @PostMapping("/reward")
    @Operation(summary = "发放邀请奖励")
    public CommonResult<Boolean> reward(String inviteCode, String deviceId) {
        return success(inviteService.rewardInvite(inviteCode, getLoginUserId(), deviceId, ServletUtils.getClientIP()));
    }
}
