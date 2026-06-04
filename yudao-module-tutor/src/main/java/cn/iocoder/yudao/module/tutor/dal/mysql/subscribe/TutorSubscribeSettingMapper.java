package cn.iocoder.yudao.module.tutor.dal.mysql.subscribe;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.subscribe.TutorSubscribeSettingDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TutorSubscribeSettingMapper extends BaseMapperX<TutorSubscribeSettingDO> {

    default TutorSubscribeSettingDO selectByUserIdAndType(Long userId, String noticeType) {
        return selectOne(new LambdaQueryWrapperX<TutorSubscribeSettingDO>()
                .eq(TutorSubscribeSettingDO::getUserId, userId)
                .eq(TutorSubscribeSettingDO::getNoticeType, noticeType));
    }

    default List<TutorSubscribeSettingDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<TutorSubscribeSettingDO>()
                .eq(TutorSubscribeSettingDO::getUserId, userId)
                .orderByAsc(TutorSubscribeSettingDO::getNoticeType));
    }
}
