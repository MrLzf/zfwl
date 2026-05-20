package cn.iocoder.yudao.module.tutor.controller.app.teacher;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.teacher.vo.AppTutorTeacherProfileRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.teacher.vo.AppTutorTeacherProfileSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.service.teacher.TutorTeacherProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 教师资料")
@RestController
@RequestMapping("/tutor/teacher-profile")
@Validated
public class AppTutorTeacherProfileController {

    @Resource
    private TutorTeacherProfileService teacherProfileService;

    @GetMapping("/get")
    @Operation(summary = "获得教师资料")
    public CommonResult<AppTutorTeacherProfileRespVO> getTeacherProfile() {
        return success(convert(teacherProfileService.getTeacherProfile(getLoginUserId())));
    }

    @PostMapping("/save")
    @Operation(summary = "保存教师资料")
    public CommonResult<AppTutorTeacherProfileRespVO> saveTeacherProfile(
            @RequestBody @Valid AppTutorTeacherProfileSaveReqVO reqVO) {
        return success(convert(teacherProfileService.saveTeacherProfile(getLoginUserId(), reqVO)));
    }

    public static AppTutorTeacherProfileRespVO convert(TutorTeacherProfileDO profile) {
        if (profile == null) {
            return null;
        }
        return AppTutorTeacherProfileRespVO.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .profileId(profile.getProfileId())
                .educationLevel(profile.getEducationLevel())
                .schoolName(profile.getSchoolName())
                .major(profile.getMajor())
                .hasTeacherCertificate(profile.getHasTeacherCertificate())
                .subjects(profile.getSubjects())
                .teachModes(profile.getTeachModes())
                .hourlyPriceMin(profile.getHourlyPriceMin())
                .hourlyPriceMax(profile.getHourlyPriceMax())
                .serviceRadiusKm(profile.getServiceRadiusKm())
                .freeTrialEnabled(profile.getFreeTrialEnabled())
                .freeTrialMinutes(profile.getFreeTrialMinutes())
                .teachingYears(profile.getTeachingYears())
                .intro(profile.getIntro())
                .certificationStatus(profile.getCertificationStatus())
                .certificationStatusName(getAuditStatusName(profile.getCertificationStatus()))
                .ratingAvg(profile.getRatingAvg())
                .reviewCount(profile.getReviewCount())
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
