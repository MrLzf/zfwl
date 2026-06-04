package cn.iocoder.yudao.module.tutor.controller.app.recharge;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import cn.iocoder.yudao.module.tutor.controller.app.recharge.vo.AppTutorPointPackageRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.recharge.vo.AppTutorPointRechargeCreateReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.recharge.vo.AppTutorPointRechargeOrderRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.recharge.TutorPointPackageDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.recharge.TutorPointRechargeOrderDO;
import cn.iocoder.yudao.module.tutor.service.recharge.TutorPointRechargeService;
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

@Tag(name = "用户 App - 家教积分充值")
@RestController
@RequestMapping("/tutor/point-recharge")
@Validated
public class AppTutorPointRechargeController {

    @Resource
    private TutorPointRechargeService rechargeService;

    @GetMapping("/packages")
    @Operation(summary = "获得积分包列表")
    public CommonResult<List<AppTutorPointPackageRespVO>> getPackages() {
        return success(rechargeService.getEnabledPackageList().stream().map(this::convert).collect(Collectors.toList()));
    }

    @PostMapping("/orders")
    @Operation(summary = "创建积分充值订单")
    public CommonResult<AppTutorPointRechargeOrderRespVO> createOrder(@Valid @RequestBody AppTutorPointRechargeCreateReqVO reqVO) {
        return success(convert(rechargeService.createOrder(getLoginUserId(), reqVO.getPackageId(), ServletUtils.getClientIP())));
    }

    @PostMapping("/pay-notify")
    @Operation(summary = "支付回调：积分到账")
    public CommonResult<Boolean> notifyPaid(@Valid @RequestBody PayOrderNotifyReqDTO notifyReqDTO) {
        rechargeService.notifyPaid(notifyReqDTO);
        return success(true);
    }

    private AppTutorPointPackageRespVO convert(TutorPointPackageDO pack) {
        int bonus = pack.getBonusPoint() == null ? 0 : pack.getBonusPoint();
        return AppTutorPointPackageRespVO.builder().id(pack.getId()).name(pack.getName())
                .point(pack.getPoint()).bonusPoint(bonus).totalPoint(pack.getPoint() + bonus)
                .price(pack.getPrice()).build();
    }

    private AppTutorPointRechargeOrderRespVO convert(TutorPointRechargeOrderDO order) {
        return AppTutorPointRechargeOrderRespVO.builder().id(order.getId()).payOrderId(order.getPayOrderId())
                .merchantOrderId(order.getMerchantOrderId()).totalPoint(order.getTotalPoint())
                .price(order.getPrice()).status(order.getStatus()).expireTime(order.getExpireTime()).build();
    }
}
