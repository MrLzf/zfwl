package cn.iocoder.yudao.module.tutor.dal.mysql.message;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.message.TutorChatMessageDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface TutorChatMessageMapper extends BaseMapperX<TutorChatMessageDO> {

    default PageResult<TutorChatMessageDO> selectRecentPage(Long userId, Long receiverUserId, PageParam pageParam,
                                                           LocalDateTime since) {
        return selectPage(pageParam, new LambdaQueryWrapperX<TutorChatMessageDO>()
                .and(wrapper -> wrapper
                        .eq(TutorChatMessageDO::getUserId, userId).eq(TutorChatMessageDO::getReceiverUserId, receiverUserId)
                        .or()
                        .eq(TutorChatMessageDO::getUserId, receiverUserId).eq(TutorChatMessageDO::getReceiverUserId, userId))
                .ge(TutorChatMessageDO::getCreateTime, since)
                .orderByDesc(TutorChatMessageDO::getId));
    }
}
