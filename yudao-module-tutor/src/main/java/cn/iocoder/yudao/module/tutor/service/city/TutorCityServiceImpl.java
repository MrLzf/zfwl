package cn.iocoder.yudao.module.tutor.service.city;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.city.TutorCityMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.CITY_NOT_EXISTS;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.CITY_NOT_OPENED;

/**
 * 家教城市 Service 实现。
 */
@Service
@Validated
public class TutorCityServiceImpl implements TutorCityService {

    @Resource
    private TutorCityMapper cityMapper;

    @Override
    public List<TutorCityDO> getEnabledCityList() {
        return cityMapper.selectEnabledList();
    }

    @Override
    public List<TutorCityDO> getCityList() {
        return cityMapper.selectList();
    }

    @Override
    public void updateCity(Long id, Boolean opened, Boolean hot, Integer sort, Integer status) {
        TutorCityDO city = cityMapper.selectById(id);
        if (city == null) {
            throw exception(CITY_NOT_EXISTS);
        }
        cityMapper.updateById(TutorCityDO.builder()
                .id(id)
                .opened(opened)
                .hot(hot)
                .sort(sort)
                .status(status)
                .build());
    }

    @Override
    public TutorCityDO validateCityOpened(String code) {
        TutorCityDO city = cityMapper.selectByCode(code);
        if (city == null || CommonStatusEnum.isDisable(city.getStatus())) {
            throw exception(CITY_NOT_EXISTS);
        }
        if (!Boolean.TRUE.equals(city.getOpened())) {
            throw exception(CITY_NOT_OPENED);
        }
        return city;
    }

}
