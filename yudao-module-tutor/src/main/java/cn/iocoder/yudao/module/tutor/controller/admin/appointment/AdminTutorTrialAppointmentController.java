package cn.iocoder.yudao.module.tutor.controller.admin.appointment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 家教预约管理")
@RestController
@RequestMapping("/tutor/trial-appointment")
@Validated
public class AdminTutorTrialAppointmentController {

    @GetMapping("/page")
    @Operation(summary = "获得试课预约分页")
    @PreAuthorize("@ss.hasPermission('tutor:appointment:query')")
    public CommonResult<PageResult<Map<String, Object>>> getAppointmentPage() {
        return success(new PageResult<>(Collections.emptyList(), 0L));
    }
}
