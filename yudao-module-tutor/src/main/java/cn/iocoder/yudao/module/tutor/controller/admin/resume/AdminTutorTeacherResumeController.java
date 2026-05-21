package cn.iocoder.yudao.module.tutor.controller.admin.resume;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.publish.vo.AdminTutorPublishAuditReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.resume.vo.AdminTutorTeacherResumePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.resume.AppTutorTeacherResumeController;
import cn.iocoder.yudao.module.tutor.controller.app.resume.vo.AppTutorTeacherResumeRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.service.resume.TutorTeacherResumeService;
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

@Tag(name = "管理后台 - 教师简历")
@RestController
@RequestMapping("/tutor/resume")
@Validated
public class AdminTutorTeacherResumeController {

    @Resource
    private TutorTeacherResumeService resumeService;

    @GetMapping("/page")
    @Operation(summary = "获得教师简历分页")
    @PreAuthorize("@ss.hasPermission('tutor:resume:query')")
    public CommonResult<PageResult<AppTutorTeacherResumeRespVO>> getResumePage(
            @Valid AdminTutorTeacherResumePageReqVO pageReqVO) {
        PageResult<TutorTeacherResumeDO> pageResult = resumeService.getResumePage(pageReqVO);
        return success(new PageResult<>(
                pageResult.getList().stream().map(AppTutorTeacherResumeController::convert).collect(Collectors.toList()),
                pageResult.getTotal()));
    }

    @PutMapping("/audit")
    @Operation(summary = "审核教师简历")
    @PreAuthorize("@ss.hasPermission('tutor:resume:audit')")
    public CommonResult<AppTutorTeacherResumeRespVO> auditResume(@RequestBody @Valid AdminTutorPublishAuditReqVO reqVO) {
        return success(AppTutorTeacherResumeController.convert(resumeService.auditResume(getLoginUserId(), reqVO)));
    }

}
