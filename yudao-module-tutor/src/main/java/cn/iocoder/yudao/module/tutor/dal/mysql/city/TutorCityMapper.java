package cn.iocoder.yudao.module.tutor.dal.mysql.city;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 家教城市 Mapper。
 */
@Mapper
public interface TutorCityMapper extends BaseMapperX<TutorCityDO> {

    default TutorCityDO selectByCode(String code) {
        return selectOne(TutorCityDO::getCode, code);
    }

    default List<TutorCityDO> selectEnabledList() {
        return selectList(new LambdaQueryWrapperX<TutorCityDO>()
                .eq(TutorCityDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByDesc(TutorCityDO::getHot)
                .orderByAsc(TutorCityDO::getSort)
                .orderByAsc(TutorCityDO::getId));
    }

}
