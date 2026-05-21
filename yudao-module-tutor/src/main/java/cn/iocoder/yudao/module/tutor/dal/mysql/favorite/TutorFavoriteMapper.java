package cn.iocoder.yudao.module.tutor.dal.mysql.favorite;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.favorite.TutorFavoriteDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TutorFavoriteMapper extends BaseMapperX<TutorFavoriteDO> {

    default TutorFavoriteDO selectByUserAndTarget(Long userId, String targetType, Long targetId) {
        return selectOne(new LambdaQueryWrapperX<TutorFavoriteDO>()
                .eq(TutorFavoriteDO::getUserId, userId)
                .eq(TutorFavoriteDO::getTargetType, targetType)
                .eq(TutorFavoriteDO::getTargetId, targetId));
    }

    default List<TutorFavoriteDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<TutorFavoriteDO>()
                .eq(TutorFavoriteDO::getUserId, userId)
                .orderByDesc(TutorFavoriteDO::getId));
    }

    default Long selectCountByUserId(Long userId) {
        return selectCount(TutorFavoriteDO::getUserId, userId);
    }

}
