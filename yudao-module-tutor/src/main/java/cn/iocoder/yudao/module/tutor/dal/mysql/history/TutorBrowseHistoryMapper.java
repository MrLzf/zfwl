package cn.iocoder.yudao.module.tutor.dal.mysql.history;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.history.TutorBrowseHistoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TutorBrowseHistoryMapper extends BaseMapperX<TutorBrowseHistoryDO> {

    default List<TutorBrowseHistoryDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<TutorBrowseHistoryDO>()
                .eq(TutorBrowseHistoryDO::getUserId, userId)
                .orderByDesc(TutorBrowseHistoryDO::getId)
                .last("LIMIT 100"));
    }

}
