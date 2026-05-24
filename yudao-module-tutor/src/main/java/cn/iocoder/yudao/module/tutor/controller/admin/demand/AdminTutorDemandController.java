package cn.iocoder.yudao.module.tutor.controller.admin.demand;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.demand.vo.AdminTutorDemandPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.publish.vo.AdminTutorPublishAuditReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.demand.AppTutorDemandController;
import cn.iocoder.yudao.module.tutor.controller.app.demand.vo.AppTutorDemandRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.service.demand.TutorDemandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 家长需求")
@RestController
@RequestMapping("/tutor/demand")
@Validated
public class AdminTutorDemandController {

    @Resource
    private TutorDemandService demandService;

    @GetMapping("/page")
    @Operation(summary = "获得家长需求分页")
    @PreAuthorize("@ss.hasPermission('tutor:demand:query')")
    public CommonResult<PageResult<AppTutorDemandRespVO>> getDemandPage(@Valid AdminTutorDemandPageReqVO pageReqVO) {
        PageResult<TutorDemandDO> pageResult = demandService.getDemandPage(pageReqVO);
        return success(new PageResult<>(
                pageResult.getList().stream().map(AppTutorDemandController::convert).collect(Collectors.toList()),
                pageResult.getTotal()));
    }

    @PutMapping("/audit")
    @Operation(summary = "审核家长需求")
    @PreAuthorize("@ss.hasPermission('tutor:demand:audit')")
    public CommonResult<AppTutorDemandRespVO> auditDemand(@RequestBody @Valid AdminTutorPublishAuditReqVO reqVO) {
        return success(AppTutorDemandController.convert(demandService.auditDemand(getLoginUserId(), reqVO)));
    }

    @PutMapping("/offline")
    @Operation(summary = "下架家长需求")
    @PreAuthorize("@ss.hasPermission('tutor:demand:offline')")
    public CommonResult<AppTutorDemandRespVO> offlineDemand(@RequestParam("id") Long id) {
        return success(AppTutorDemandController.convert(demandService.offlineDemandByAdmin(id)));
    }

}
