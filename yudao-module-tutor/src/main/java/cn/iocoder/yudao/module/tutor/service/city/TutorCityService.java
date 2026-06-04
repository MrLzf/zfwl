package cn.iocoder.yudao.module.tutor.service.city;

import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;

import java.util.List;

/**
 * 家教城市 Service 接口。
 */
public interface TutorCityService {

    /**
     * 获得启用城市列表。
     *
     * @return 城市列表
     */
    List<TutorCityDO> getEnabledCityList();

    /**
     * 获得全部城市列表。
     *
     * @return 城市列表
     */
    List<TutorCityDO> getCityList();

    /**
     * 更新城市运营状态。
     *
     * @param id 城市编号
     * @param opened 是否开通
     * @param hot 是否热门
     * @param sort 排序
     * @param status 状态
     */
    void updateCity(Long id, Boolean opened, Boolean hot, Integer sort, Integer status);

    /**
     * 更新城市运营规则。
     *
     * @param id 城市编号
     * @param serviceConfig 城市运营配置 JSON
     */
    void updateCityRules(Long id, String serviceConfig);

    /**
     * 校验城市已开通。
     *
     * @param code 城市编码
     * @return 城市
     */
    TutorCityDO validateCityOpened(String code);

}
