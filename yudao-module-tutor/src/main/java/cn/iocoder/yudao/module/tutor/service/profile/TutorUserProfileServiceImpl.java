package cn.iocoder.yudao.module.tutor.service.profile;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.tutor.controller.admin.profile.vo.AdminTutorUserProfilePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileInitReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileLocationUpdateReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.profile.TutorUserProfileMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.teacher.TutorTeacherProfileMapper;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import cn.iocoder.yudao.module.tutor.service.city.TutorCityService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.PROFILE_NOT_EXISTS;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.PROFILE_ROLE_INVALID;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.PROFILE_ROLE_SELECTED;

/**
 * 家教用户档案 Service 实现。
 */
@Service
@Validated
public class TutorUserProfileServiceImpl implements TutorUserProfileService {

    @Resource
    private TutorUserProfileMapper profileMapper;
    @Resource
    private TutorTeacherProfileMapper teacherProfileMapper;
    @Resource
    private TutorCityService cityService;
    @Resource
    private MemberUserApi memberUserApi;

    @Override
    public TutorUserProfileDO getProfile(Long userId) {
        return profileMapper.selectByUserId(userId);
    }

    @Override
    public TutorUserProfileDO initProfile(Long userId, AppTutorProfileInitReqVO reqVO) {
        TutorUserProfileDO profile = profileMapper.selectByUserId(userId);
        if (profile != null) {
            throw exception(PROFILE_ROLE_SELECTED);
        }
        validateRole(reqVO.getRole());
        TutorCityDO city = cityService.validateCityOpened(reqVO.getCityCode());
        profile = TutorUserProfileDO.builder()
                .userId(userId)
                .role(reqVO.getRole())
                .cityId(city.getId())
                .cityCode(city.getCode())
                .cityName(city.getName())
                .status(CommonStatusEnum.ENABLE.getStatus())
                .build();
        profileMapper.insert(profile);
        return profile;
    }

    @Override
    public TutorUserProfileDO updateLocation(Long userId, AppTutorProfileLocationUpdateReqVO reqVO) {
        TutorUserProfileDO profile = profileMapper.selectByUserId(userId);
        if (profile == null) {
            throw exception(PROFILE_NOT_EXISTS);
        }
        TutorUserProfileDO updateObj = TutorUserProfileDO.builder()
                .id(profile.getId())
                .longitude(reqVO.getLongitude())
                .latitude(reqVO.getLatitude())
                .locationAddress(reqVO.getLocationAddress())
                .locationTime(LocalDateTime.now())
                .build();
        if (StrUtil.isNotBlank(reqVO.getCityCode())) {
            TutorCityDO city = cityService.validateCityOpened(reqVO.getCityCode());
            updateObj.setCityId(city.getId());
            updateObj.setCityCode(city.getCode());
            updateObj.setCityName(city.getName());
        }
        profileMapper.updateById(updateObj);
        return profileMapper.selectById(profile.getId());
    }

    @Override
    public PageResult<TutorUserProfileDO> getProfilePage(AdminTutorUserProfilePageReqVO reqVO) {
        Set<Long> userIds = null;
        if (StrUtil.isNotBlank(reqVO.getMobile())) {
            MemberUserRespDTO user = memberUserApi.getUserByMobile(reqVO.getMobile());
            if (user == null) {
                return PageResult.empty();
            }
            userIds = new HashSet<>();
            userIds.add(user.getId());
        }
        if (StrUtil.isNotBlank(reqVO.getNickname())) {
            List<MemberUserRespDTO> users = memberUserApi.getUserListByNickname(reqVO.getNickname());
            Set<Long> nicknameUserIds = users.stream().map(MemberUserRespDTO::getId).collect(Collectors.toSet());
            userIds = intersectUserIds(userIds, nicknameUserIds);
            if (CollUtil.isEmpty(userIds)) {
                return PageResult.empty();
            }
        }
        if (reqVO.getCertificationStatus() != null) {
            List<TutorTeacherProfileDO> teacherProfiles = teacherProfileMapper.selectListByCertificationStatus(reqVO.getCertificationStatus());
            Set<Long> certificationUserIds = teacherProfiles.stream().map(TutorTeacherProfileDO::getUserId).collect(Collectors.toSet());
            userIds = intersectUserIds(userIds, certificationUserIds);
            if (CollUtil.isEmpty(userIds)) {
                return PageResult.empty();
            }
        }
        return profileMapper.selectPage(reqVO, userIds);
    }

    private Set<Long> intersectUserIds(Set<Long> source, Set<Long> filter) {
        if (source == null) {
            return filter;
        }
        source.retainAll(filter);
        return source;
    }

    private void validateRole(Integer role) {
        for (Integer roleValue : TutorUserRoleEnum.ARRAYS) {
            if (roleValue.equals(role)) {
                return;
            }
        }
        throw exception(PROFILE_ROLE_INVALID);
    }

}
