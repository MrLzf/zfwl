package cn.iocoder.yudao.module.tutor.service.review;

import cn.iocoder.yudao.module.tutor.controller.app.review.vo.AppTutorReviewCreateReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.review.TutorReviewDO;

import java.util.List;

public interface TutorReviewService {
    TutorReviewDO createReview(Long reviewerUserId, AppTutorReviewCreateReqVO reqVO);
    List<TutorReviewDO> getMyReviewList(Long reviewerUserId);
    List<TutorReviewDO> getTargetReviewList(Long targetUserId);
}
