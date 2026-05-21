package cn.iocoder.yudao.module.tutor.dal.mysql.review;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.controller.admin.review.vo.AdminTutorReviewPageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.review.TutorReviewDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TutorReviewMapper extends BaseMapperX<TutorReviewDO> {

    default TutorReviewDO selectByMatchIdAndReviewerUserId(Long matchId, Long reviewerUserId) {
        return selectOne(new LambdaQueryWrapperX<TutorReviewDO>()
                .eq(TutorReviewDO::getMatchId, matchId)
                .eq(TutorReviewDO::getReviewerUserId, reviewerUserId));
    }

    default List<TutorReviewDO> selectListByReviewerUserId(Long reviewerUserId) {
        return selectList(new LambdaQueryWrapperX<TutorReviewDO>()
                .eq(TutorReviewDO::getReviewerUserId, reviewerUserId)
                .orderByDesc(TutorReviewDO::getId));
    }

    default List<TutorReviewDO> selectVisibleListByTargetUserId(Long targetUserId) {
        return selectList(new LambdaQueryWrapperX<TutorReviewDO>()
                .eq(TutorReviewDO::getTargetUserId, targetUserId)
                .eq(TutorReviewDO::getStatus, 0)
                .orderByDesc(TutorReviewDO::getId));
    }

    default PageResult<TutorReviewDO> selectPage(AdminTutorReviewPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TutorReviewDO>()
                .eqIfPresent(TutorReviewDO::getMatchId, reqVO.getMatchId())
                .eqIfPresent(TutorReviewDO::getReviewerUserId, reqVO.getReviewerUserId())
                .eqIfPresent(TutorReviewDO::getTargetUserId, reqVO.getTargetUserId())
                .eqIfPresent(TutorReviewDO::getRating, reqVO.getRating())
                .eqIfPresent(TutorReviewDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(TutorReviewDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TutorReviewDO::getId));
    }

}
