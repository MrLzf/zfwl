package cn.iocoder.yudao.module.tutor.dal.mysql.point;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.point.TutorPointRewardRecordDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TutorPointRewardRecordMapper extends BaseMapperX<TutorPointRewardRecordDO> {

    default TutorPointRewardRecordDO selectByUniqueKey(Long userId, String taskType, String bizId) {
        return selectOne(TutorPointRewardRecordDO::getUserId, userId,
                TutorPointRewardRecordDO::getTaskType, taskType,
                TutorPointRewardRecordDO::getBizId, bizId);
    }

    default boolean existsByTaskType(Long userId, String taskType) {
        return selectCount(new LambdaQueryWrapperX<TutorPointRewardRecordDO>()
                .eq(TutorPointRewardRecordDO::getUserId, userId)
                .eq(TutorPointRewardRecordDO::getTaskType, taskType)) > 0;
    }

}
