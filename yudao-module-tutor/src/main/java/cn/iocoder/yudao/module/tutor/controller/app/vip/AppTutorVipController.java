package cn.iocoder.yudao.module.tutor.controller.app.vip;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.vip.vo.AppTutorVipBuyReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.vip.vo.AppTutorVipRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.vip.TutorVipRecordDO;
import cn.iocoder.yudao.module.tutor.service.vip.TutorVipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 家教 VIP")
@RestController
@RequestMapping("/tutor/vip")
@Validated
public class AppTutorVipController {

    @Resource
    private TutorVipService vipService;

    @GetMapping("/my")
    @Operation(summary = "获得我的 VIP 权益")
    public CommonResult<AppTutorVipRespVO> getMyVip() {
        return success(convert(vipService.getActiveVip(getLoginUserId())));
    }

    @PostMapping("/buy")
    @Operation(summary = "购买 VIP")
    public CommonResult<AppTutorVipRespVO> buyVip(@RequestBody @Valid AppTutorVipBuyReqVO reqVO) {
        return success(convert(vipService.buyVip(getLoginUserId(), reqVO.getConfigId())));
    }

    private AppTutorVipRespVO convert(TutorVipRecordDO record) {
        if (record == null) {
            return AppTutorVipRespVO.builder().active(false).build();
        }
        return AppTutorVipRespVO.builder().active(true).id(record.getId()).startTime(record.getStartTime())
                .endTime(record.getEndTime()).monthlyGiftPoint(record.getMonthlyGiftPoint()).build();
    }
}
