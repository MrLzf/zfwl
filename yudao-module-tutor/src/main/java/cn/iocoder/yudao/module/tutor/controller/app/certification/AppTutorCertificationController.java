package cn.iocoder.yudao.module.tutor.controller.app.certification;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.certification.vo.AppTutorCertificationRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.certification.vo.AppTutorCertificationSubmitReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.certification.TutorCertificationDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.service.certification.TutorCertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 教师认证")
@RestController
@RequestMapping("/tutor/certification")
@Validated
public class AppTutorCertificationController {

    @Resource
    private TutorCertificationService certificationService;

    @GetMapping("/my")
    @Operation(summary = "获得我的教师认证")
    public CommonResult<AppTutorCertificationRespVO> getMyCertification() {
        return success(convert(certificationService.getCertification(getLoginUserId())));
    }

    @PostMapping("/submit")
    @Operation(summary = "提交教师认证")
    public CommonResult<AppTutorCertificationRespVO> submitCertification(
            @RequestBody @Valid AppTutorCertificationSubmitReqVO reqVO) {
        return success(convert(certificationService.submitCertification(getLoginUserId(), reqVO)));
    }

    public static AppTutorCertificationRespVO convert(TutorCertificationDO certification) {
        if (certification == null) {
            return null;
        }
        return AppTutorCertificationRespVO.builder()
                .id(certification.getId())
                .userId(certification.getUserId())
                .teacherProfileId(certification.getTeacherProfileId())
                .realName(certification.getRealName())
                .idCardNoMask(certification.getIdCardNoMask())
                .educationFileUrl(certification.getEducationFileUrl())
                .teacherCertFileUrl(certification.getTeacherCertFileUrl())
                .status(certification.getStatus())
                .statusName(getAuditStatusName(certification.getStatus()))
                .rejectReason(certification.getRejectReason())
                .auditorId(certification.getAuditorId())
                .auditTime(certification.getAuditTime())
                .createTime(certification.getCreateTime())
                .build();
    }

    private static String getAuditStatusName(Integer status) {
        for (TutorAuditStatusEnum statusEnum : TutorAuditStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum.getName();
            }
        }
        return null;
    }

}
