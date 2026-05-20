package cn.iocoder.yudao.module.tutor.dal.mysql.demand;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface TutorDemandMapper extends BaseMapperX<TutorDemandDO> {

    default List<TutorDemandDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<TutorDemandDO>()
                .eq(TutorDemandDO::getUserId, userId)
                .orderByDesc(TutorDemandDO::getId));
    }

    default Long selectCountByUserIdAndStatuses(Long userId, Collection<Integer> statuses) {
        return selectCount(new LambdaQueryWrapperX<TutorDemandDO>()
                .eq(TutorDemandDO::getUserId, userId)
                .in(TutorDemandDO::getStatus, statuses));
    }

}
