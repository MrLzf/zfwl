package cn.iocoder.yudao.module.tutor.controller.app.escrow;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - 家教担保交易")
@RestController
@RequestMapping("/tutor/escrow-trades")
@Validated
public class AppTutorEscrowTradeController {

    @PostMapping
    @Operation(summary = "创建担保交易")
    public CommonResult<Boolean> createEscrowTrade(@RequestBody Map<String, Object> reqVO) {
        return success(true);
    }

    @GetMapping("/my")
    @Operation(summary = "我的担保交易")
    public CommonResult<List<Map<String, Object>>> getMyEscrowTrades() {
        return success(Collections.emptyList());
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "确认完成并申请释放")
    public CommonResult<Boolean> confirmEscrowComplete(@PathVariable("id") Long id) {
        return success(true);
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "申请退款/争议")
    public CommonResult<Boolean> applyEscrowRefund(@PathVariable("id") Long id,
                                                   @RequestBody Map<String, Object> reqVO) {
        return success(true);
    }
}
