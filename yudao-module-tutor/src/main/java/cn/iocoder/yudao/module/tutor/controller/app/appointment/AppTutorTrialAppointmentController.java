package cn.iocoder.yudao.module.tutor.controller.app.appointment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - 家教预约试课")
@RestController
@RequestMapping("/tutor/trial-appointments")
@Validated
public class AppTutorTrialAppointmentController {

    @PostMapping
    @Operation(summary = "创建试课预约")
    public CommonResult<Boolean> createTrialAppointment(@RequestBody Map<String, Object> reqVO) {
        return success(true);
    }

    @GetMapping("/my")
    @Operation(summary = "我的试课预约")
    public CommonResult<List<Map<String, Object>>> getMyTrialAppointments() {
        return success(Collections.emptyList());
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "确认试课预约")
    public CommonResult<Boolean> confirmTrialAppointment(@PathVariable("id") Long id) {
        return success(true);
    }
}
