package cn.iocoder.yudao.module.tutor.controller.app.parent;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.parent.vo.AppTutorParentProfileRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.parent.vo.AppTutorParentProfileSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.parent.TutorParentProfileDO;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorTeachModeEnum;
import cn.iocoder.yudao.module.tutor.service.parent.TutorParentProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 家长资料")
@RestController
@RequestMapping("/tutor/parent-profile")
@Validated
public class AppTutorParentProfileController {

    @Resource
    private TutorParentProfileService parentProfileService;

    @GetMapping("/get")
    @Operation(summary = "获得家长资料")
    public CommonResult<AppTutorParentProfileRespVO> getParentProfile() {
        return success(convert(parentProfileService.getParentProfile(getLoginUserId())));
    }

    @PostMapping("/save")
    @Operation(summary = "保存家长资料")
    public CommonResult<AppTutorParentProfileRespVO> saveParentProfile(
            @RequestBody @Valid AppTutorParentProfileSaveReqVO reqVO) {
        return success(convert(parentProfileService.saveParentProfile(getLoginUserId(), reqVO)));
    }

    private AppTutorParentProfileRespVO convert(TutorParentProfileDO profile) {
        if (profile == null) {
            return null;
        }
        return AppTutorParentProfileRespVO.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .profileId(profile.getProfileId())
                .childGrade(profile.getChildGrade())
                .subjects(profile.getSubjects())
                .budgetMin(profile.getBudgetMin())
                .budgetMax(profile.getBudgetMax())
                .teachMode(profile.getTeachMode())
                .teachModeName(getTeachModeName(profile.getTeachMode()))
                .remark(profile.getRemark())
                .build();
    }

    private String getTeachModeName(Integer teachMode) {
        for (TutorTeachModeEnum modeEnum : TutorTeachModeEnum.values()) {
            if (modeEnum.getMode().equals(teachMode)) {
                return modeEnum.getName();
            }
        }
        return null;
    }

}
