package cn.iocoder.yudao.module.tutor.service.profile;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileInitReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileLocationUpdateReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.profile.TutorUserProfileMapper;
import cn.iocoder.yudao.module.tutor.service.city.TutorCityService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.PROFILE_NOT_EXISTS;
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
    private TutorCityService cityService;

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

}
