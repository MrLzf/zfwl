package cn.iocoder.yudao.module.tutor.controller.app.resume;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.resume.vo.AppTutorTeacherResumeRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.resume.vo.AppTutorTeacherResumeSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import cn.iocoder.yudao.module.tutor.service.resume.TutorTeacherResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 教师简历")
@RestController
@RequestMapping("/tutor/resumes")
@Validated
public class AppTutorTeacherResumeController {

    @Resource
    private TutorTeacherResumeService resumeService;

    @PostMapping
    @Operation(summary = "发布教师简历")
    public CommonResult<AppTutorTeacherResumeRespVO> createResume(
            @RequestBody @Valid AppTutorTeacherResumeSaveReqVO reqVO) {
        return success(convert(resumeService.createResume(getLoginUserId(), reqVO)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改教师简历")
    @Parameter(name = "id", description = "简历编号", required = true)
    public CommonResult<AppTutorTeacherResumeRespVO> updateResume(@PathVariable("id") Long id,
                                                                  @RequestBody @Valid AppTutorTeacherResumeSaveReqVO reqVO) {
        return success(convert(resumeService.updateResume(getLoginUserId(), id, reqVO)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "下架教师简历")
    @Parameter(name = "id", description = "简历编号", required = true)
    public CommonResult<Boolean> offlineResume(@PathVariable("id") Long id) {
        resumeService.offlineResume(getLoginUserId(), id);
        return success(true);
    }

    @GetMapping("/my")
    @Operation(summary = "获得我的教师简历列表")
    public CommonResult<List<AppTutorTeacherResumeRespVO>> getMyResumeList() {
        return success(resumeService.getMyResumeList(getLoginUserId()).stream()
                .map(AppTutorTeacherResumeController::convert)
                .collect(Collectors.toList()));
    }

    public static AppTutorTeacherResumeRespVO convert(TutorTeacherResumeDO resume) {
        if (resume == null) {
            return null;
        }
        return AppTutorTeacherResumeRespVO.builder()
                .id(resume.getId())
                .userId(resume.getUserId())
                .title(resume.getTitle())
                .subjects(resume.getSubjects())
                .teachModes(resume.getTeachModes())
                .hourlyPrice(resume.getHourlyPrice())
                .freeTrialEnabled(resume.getFreeTrialEnabled())
                .freeTrialMinutes(resume.getFreeTrialMinutes())
                .teachingExperience(resume.getTeachingExperience())
                .availableTimes(resume.getAvailableTimes())
                .cityCode(resume.getCityCode())
                .cityName(resume.getCityName())
                .longitude(resume.getLongitude())
                .latitude(resume.getLatitude())
                .serviceRadiusKm(resume.getServiceRadiusKm())
                .contactMobileMask(resume.getContactMobileMask())
                .contactWechatMask(resume.getContactWechatMask())
                .status(resume.getStatus())
                .statusName(getPublishStatusName(resume.getStatus()))
                .auditStatus(resume.getAuditStatus())
                .auditStatusName(getAuditStatusName(resume.getAuditStatus()))
                .rejectReason(resume.getRejectReason())
                .ratingAvg(resume.getRatingAvg())
                .reviewCount(resume.getReviewCount())
                .viewCount(resume.getViewCount())
                .contactViewCount(resume.getContactViewCount())
                .matchCount(resume.getMatchCount())
                .createTime(resume.getCreateTime())
                .build();
    }

    private static String getPublishStatusName(Integer status) {
        for (TutorPublishStatusEnum statusEnum : TutorPublishStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum.getName();
            }
        }
        return null;
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
