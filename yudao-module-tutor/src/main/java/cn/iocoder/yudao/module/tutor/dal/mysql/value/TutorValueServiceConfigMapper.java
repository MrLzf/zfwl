package cn.iocoder.yudao.module.tutor.dal.mysql.value;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.value.TutorValueServiceConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TutorValueServiceConfigMapper extends BaseMapperX<TutorValueServiceConfigDO> {

    default List<TutorValueServiceConfigDO> selectEnabledList(String targetType) {
        return selectList(new LambdaQueryWrapperX<TutorValueServiceConfigDO>()
                .eq(TutorValueServiceConfigDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .eqIfPresent(TutorValueServiceConfigDO::getTargetType, targetType)
                .orderByAsc(TutorValueServiceConfigDO::getId));
    }
}
