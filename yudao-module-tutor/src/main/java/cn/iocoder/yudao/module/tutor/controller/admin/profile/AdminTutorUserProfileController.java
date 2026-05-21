package cn.iocoder.yudao.module.tutor.controller.admin.profile;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.tutor.controller.admin.profile.vo.AdminTutorUserProfilePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.profile.vo.AdminTutorUserProfileRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import cn.iocoder.yudao.module.tutor.service.profile.TutorUserProfileService;
import cn.iocoder.yudao.module.tutor.service.teacher.TutorTeacherProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 家教用户档案")
@RestController
@RequestMapping("/tutor/profiles")
@Validated
public class AdminTutorUserProfileController {

    @Resource
    private TutorUserProfileService profileService;
    @Resource
    private TutorTeacherProfileService teacherProfileService;
    @Resource
    private MemberUserApi memberUserApi;

    @GetMapping("/page")
    @Operation(summary = "获得家教用户档案分页")
    @PreAuthorize("@ss.hasPermission('tutor:profile:query')")
    public CommonResult<PageResult<AdminTutorUserProfileRespVO>> getProfilePage(
            @Valid AdminTutorUserProfilePageReqVO pageReqVO) {
        PageResult<TutorUserProfileDO> pageResult = profileService.getProfilePage(pageReqVO);
        Set<Long> userIds = pageResult.getList().stream().map(TutorUserProfileDO::getUserId).collect(Collectors.toSet());
        Map<Long, MemberUserRespDTO> userMap = userIds.isEmpty() ? Collections.emptyMap() : memberUserApi.getUserMap(userIds);
        return success(new PageResult<>(pageResult.getList().stream()
                .map(profile -> convert(profile, userMap.get(profile.getUserId())))
                .collect(Collectors.toList()), pageResult.getTotal()));
    }

    private AdminTutorUserProfileRespVO convert(TutorUserProfileDO profile, MemberUserRespDTO user) {
        TutorTeacherProfileDO teacherProfile = teacherProfileService.getTeacherProfile(profile.getUserId());
        return AdminTutorUserProfileRespVO.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .nickname(user == null ? null : user.getNickname())
                .mobile(user == null ? null : user.getMobile())
                .point(user == null ? null : user.getPoint())
                .role(profile.getRole())
                .cityCode(profile.getCityCode())
                .cityName(profile.getCityName())
                .longitude(profile.getLongitude())
                .latitude(profile.getLatitude())
                .locationAddress(profile.getLocationAddress())
                .locationTime(profile.getLocationTime())
                .status(profile.getStatus())
                .certificationStatus(teacherProfile == null ? null : teacherProfile.getCertificationStatus())
                .createTime(profile.getCreateTime())
                .build();
    }

}
