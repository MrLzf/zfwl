package cn.iocoder.yudao.module.tutor.dal.mysql.contact;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.contact.TutorContactViewRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TutorContactViewRecordMapper extends BaseMapperX<TutorContactViewRecordDO> {

    default TutorContactViewRecordDO selectReusable(Long viewerUserId, String targetType, Long targetId, LocalDateTime now) {
        return selectOne(new LambdaQueryWrapperX<TutorContactViewRecordDO>()
                .eq(TutorContactViewRecordDO::getViewerUserId, viewerUserId)
                .eq(TutorContactViewRecordDO::getTargetType, targetType)
                .eq(TutorContactViewRecordDO::getTargetId, targetId)
                .gtIfPresent(TutorContactViewRecordDO::getFreeReuseUntil, now)
                .orderByDesc(TutorContactViewRecordDO::getId)
                .last("LIMIT 1"));
    }

    default List<TutorContactViewRecordDO> selectListByViewerUserId(Long viewerUserId) {
        return selectList(new LambdaQueryWrapperX<TutorContactViewRecordDO>()
                .eq(TutorContactViewRecordDO::getViewerUserId, viewerUserId)
                .orderByDesc(TutorContactViewRecordDO::getId));
    }

}
