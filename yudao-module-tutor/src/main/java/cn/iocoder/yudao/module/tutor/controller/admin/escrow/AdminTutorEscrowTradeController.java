package cn.iocoder.yudao.module.tutor.controller.admin.escrow;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 家教交易和争议管理")
@RestController
@RequestMapping("/tutor/escrow-trade")
@Validated
public class AdminTutorEscrowTradeController {

    @GetMapping("/page")
    @Operation(summary = "获得担保交易分页")
    @PreAuthorize("@ss.hasPermission('tutor:escrow:query')")
    public CommonResult<PageResult<Map<String, Object>>> getEscrowTradePage() {
        return success(new PageResult<>(Collections.emptyList(), 0L));
    }

    @PutMapping("/{id}/handle")
    @Operation(summary = "处理退款、争议、异常订单")
    @PreAuthorize("@ss.hasPermission('tutor:escrow:handle')")
    public CommonResult<Boolean> handleEscrowTrade(@PathVariable("id") Long id,
                                                   @RequestBody Map<String, Object> reqVO) {
        return success(true);
    }
}
