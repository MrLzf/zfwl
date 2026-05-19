package cn.iocoder.yudao.module.tutor.controller.app.profile;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileInitReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileLocationUpdateReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import cn.iocoder.yudao.module.tutor.service.profile.TutorUserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 家教用户档案")
@RestController
@RequestMapping("/tutor/profile")
@Validated
public class AppTutorProfileController {

    @Resource
    private TutorUserProfileService profileService;

    @GetMapping("/get")
    @Operation(summary = "获得家教用户档案")
    public CommonResult<AppTutorProfileRespVO> getProfile() {
        return success(convert(profileService.getProfile(getLoginUserId())));
    }

    @PostMapping("/init")
    @Operation(summary = "初始化家教用户档案")
    public CommonResult<AppTutorProfileRespVO> initProfile(@RequestBody @Valid AppTutorProfileInitReqVO reqVO) {
        return success(convert(profileService.initProfile(getLoginUserId(), reqVO)));
    }

    @PutMapping("/location")
    @Operation(summary = "更新家教用户定位")
    public CommonResult<AppTutorProfileRespVO> updateLocation(@RequestBody @Valid AppTutorProfileLocationUpdateReqVO reqVO) {
        return success(convert(profileService.updateLocation(getLoginUserId(), reqVO)));
    }

    private AppTutorProfileRespVO convert(TutorUserProfileDO profile) {
        if (profile == null) {
            return null;
        }
        return AppTutorProfileRespVO.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .role(profile.getRole())
                .roleName(getRoleName(profile.getRole()))
                .cityId(profile.getCityId())
                .cityCode(profile.getCityCode())
                .cityName(profile.getCityName())
                .longitude(profile.getLongitude())
                .latitude(profile.getLatitude())
                .locationAddress(profile.getLocationAddress())
                .locationTime(profile.getLocationTime())
                .status(profile.getStatus())
                .build();
    }

    private String getRoleName(Integer role) {
        for (TutorUserRoleEnum roleEnum : TutorUserRoleEnum.values()) {
            if (roleEnum.getRole().equals(role)) {
                return roleEnum.getName();
            }
        }
        return null;
    }

}
