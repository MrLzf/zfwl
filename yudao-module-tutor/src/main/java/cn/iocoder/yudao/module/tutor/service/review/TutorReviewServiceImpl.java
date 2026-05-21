package cn.iocoder.yudao.module.tutor.service.review;

import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.tutor.controller.app.review.vo.AppTutorReviewCreateReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.match.TutorMatchRecordDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.review.TutorReviewDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.match.TutorMatchRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.review.TutorReviewMapper;
import cn.iocoder.yudao.module.tutor.enums.match.TutorMatchStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.*;

@Service
@Validated
public class TutorReviewServiceImpl implements TutorReviewService {

    private static final int FIVE_STAR_REWARD_POINT = 10;

    @Resource
    private TutorReviewMapper reviewMapper;
    @Resource
    private TutorMatchRecordMapper matchRecordMapper;
    @Resource
    private TutorTeacherResumeMapper resumeMapper;
    @Resource
    private MemberPointApi memberPointApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TutorReviewDO createReview(Long reviewerUserId, AppTutorReviewCreateReqVO reqVO) {
        TutorMatchRecordDO match = matchRecordMapper.selectById(reqVO.getMatchId());
        if (match == null) {
            throw exception(MATCH_NOT_EXISTS);
        }
        if (!TutorMatchStatusEnum.BOTH_CONFIRMED.getStatus().equals(match.getStatus())) {
            throw exception(REVIEW_NOT_ALLOWED);
        }
        if (!Objects.equals(reviewerUserId, match.getParentUserId())
                && !Objects.equals(reviewerUserId, match.getTeacherUserId())) {
            throw exception(REVIEW_NOT_ALLOWED);
        }
        if (reviewMapper.selectByMatchIdAndReviewerUserId(reqVO.getMatchId(), reviewerUserId) != null) {
            throw exception(REVIEW_EXISTS);
        }
        Long targetUserId = Objects.equals(reviewerUserId, match.getParentUserId())
                ? match.getTeacherUserId() : match.getParentUserId();
        TutorReviewDO review = TutorReviewDO.builder()
                .matchId(reqVO.getMatchId()).reviewerUserId(reviewerUserId).targetUserId(targetUserId)
                .rating(reqVO.getRating()).tags(reqVO.getTags()).content(reqVO.getContent())
                .anonymousDisplay(Boolean.TRUE.equals(reqVO.getAnonymousDisplay()))
                .status(0).build();
        reviewMapper.insert(review);
        updateResumeRatingIfNeeded(match, targetUserId, reqVO.getRating());
        if (reqVO.getRating() == 5) {
            memberPointApi.addPoint(targetUserId, FIVE_STAR_REWARD_POINT,
                    MemberPointBizTypeEnum.TUTOR_FIVE_STAR_REVIEW.getType(), String.valueOf(review.getId()));
        }
        return review;
    }

    @Override
    public List<TutorReviewDO> getMyReviewList(Long reviewerUserId) {
        return reviewMapper.selectListByReviewerUserId(reviewerUserId);
    }

    @Override
    public List<TutorReviewDO> getTargetReviewList(Long targetUserId) {
        return reviewMapper.selectVisibleListByTargetUserId(targetUserId);
    }

    private void updateResumeRatingIfNeeded(TutorMatchRecordDO match, Long targetUserId, Integer rating) {
        if (match.getResumeId() == null || !Objects.equals(targetUserId, match.getTeacherUserId())) {
            return;
        }
        TutorTeacherResumeDO resume = resumeMapper.selectById(match.getResumeId());
        if (resume == null) {
            return;
        }
        int reviewCount = resume.getReviewCount() == null ? 0 : resume.getReviewCount();
        BigDecimal ratingAvg = resume.getRatingAvg() == null ? BigDecimal.ZERO : resume.getRatingAvg();
        BigDecimal total = ratingAvg.multiply(BigDecimal.valueOf(reviewCount)).add(BigDecimal.valueOf(rating));
        BigDecimal newAvg = total.divide(BigDecimal.valueOf(reviewCount + 1), 2, RoundingMode.HALF_UP);
        resumeMapper.updateById(TutorTeacherResumeDO.builder()
                .id(resume.getId()).ratingAvg(newAvg).reviewCount(reviewCount + 1).build());
    }

}
