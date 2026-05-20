package cn.iocoder.yudao.module.tutor.controller.admin.certification;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.certification.vo.AdminTutorCertificationAuditReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.certification.vo.AdminTutorCertificationPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.certification.AppTutorCertificationController;
import cn.iocoder.yudao.module.tutor.controller.app.certification.vo.AppTutorCertificationRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.certification.TutorCertificationDO;
import cn.iocoder.yudao.module.tutor.service.certification.TutorCertificationService;
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

@Tag(name = "管理后台 - 教师认证")
@RestController
@RequestMapping("/tutor/certification")
@Validated
public class AdminTutorCertificationController {

    @Resource
    private TutorCertificationService certificationService;

    @GetMapping("/page")
    @Operation(summary = "获得教师认证分页")
    @PreAuthorize("@ss.hasPermission('tutor:certification:query')")
    public CommonResult<PageResult<AppTutorCertificationRespVO>> getCertificationPage(
            @Valid AdminTutorCertificationPageReqVO pageReqVO) {
        PageResult<TutorCertificationDO> pageResult = certificationService.getCertificationPage(pageReqVO);
        return success(new PageResult<>(
                pageResult.getList().stream().map(AppTutorCertificationController::convert).collect(Collectors.toList()),
                pageResult.getTotal()));
    }

    @PutMapping("/audit")
    @Operation(summary = "审核教师认证")
    @PreAuthorize("@ss.hasPermission('tutor:certification:audit')")
    public CommonResult<AppTutorCertificationRespVO> auditCertification(
            @RequestBody @Valid AdminTutorCertificationAuditReqVO reqVO) {
        return success(AppTutorCertificationController.convert(
                certificationService.auditCertification(getLoginUserId(), reqVO)));
    }

}
