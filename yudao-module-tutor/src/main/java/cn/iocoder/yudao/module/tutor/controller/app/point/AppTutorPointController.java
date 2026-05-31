package cn.iocoder.yudao.module.tutor.controller.app.point;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.point.vo.AppTutorPointTaskRespVO;
import cn.iocoder.yudao.module.tutor.service.point.TutorPointTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 家教积分任务")
@RestController
@RequestMapping("/tutor/points")
public class AppTutorPointController {

    @Resource
    private TutorPointTaskService pointTaskService;

    @GetMapping("/tasks")
    @Operation(summary = "获得积分任务列表")
    public CommonResult<List<AppTutorPointTaskRespVO>> getTaskList() {
        return success(pointTaskService.getTaskList(getLoginUserId()));
    }

}
