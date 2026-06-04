package cn.iocoder.yudao.module.tutor.dal.mysql.vip;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.vip.TutorVipRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TutorVipRecordMapper extends BaseMapperX<TutorVipRecordDO> {

    default TutorVipRecordDO selectActiveByUserId(Long userId, LocalDateTime now) {
        return selectOne(new LambdaQueryWrapperX<TutorVipRecordDO>()
                .eq(TutorVipRecordDO::getUserId, userId)
                .eq(TutorVipRecordDO::getStatus, 0)
                .le(TutorVipRecordDO::getStartTime, now)
                .gt(TutorVipRecordDO::getEndTime, now)
                .orderByDesc(TutorVipRecordDO::getEndTime)
                .last("LIMIT 1"));
    }

    default List<TutorVipRecordDO> selectActiveList(LocalDateTime now) {
        return selectList(new LambdaQueryWrapperX<TutorVipRecordDO>()
                .eq(TutorVipRecordDO::getStatus, 0)
                .le(TutorVipRecordDO::getStartTime, now)
                .gt(TutorVipRecordDO::getEndTime, now));
    }
}
