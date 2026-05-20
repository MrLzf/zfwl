package cn.iocoder.yudao.module.tutor.service.profile;

import cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthTutorProfileRespVO;
import cn.iocoder.yudao.module.member.service.auth.MemberTutorProfileApi;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileInitReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 会员登录扩展的家教档案实现。
 */
@Service
public class MemberTutorProfileApiImpl implements MemberTutorProfileApi {

    @Resource
    private TutorUserProfileService profileService;

    @Override
    public AppAuthTutorProfileRespVO getProfile(Long userId) {
        return convert(profileService.getProfile(userId));
    }

    @Override
    public AppAuthTutorProfileRespVO initProfileIfAbsent(Long userId, Integer role, String cityCode) {
        TutorUserProfileDO profile = profileService.getProfile(userId);
        if (profile == null) {
            AppTutorProfileInitReqVO reqVO = new AppTutorProfileInitReqVO();
            reqVO.setRole(role);
            reqVO.setCityCode(cityCode);
            profile = profileService.initProfile(userId, reqVO);
        }
        return convert(profile);
    }

    private AppAuthTutorProfileRespVO convert(TutorUserProfileDO profile) {
        if (profile == null) {
            return null;
        }
        return AppAuthTutorProfileRespVO.builder()
                .id(profile.getId())
                .role(profile.getRole())
                .roleName(getRoleName(profile.getRole()))
                .cityId(profile.getCityId())
                .cityCode(profile.getCityCode())
                .cityName(profile.getCityName())
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
