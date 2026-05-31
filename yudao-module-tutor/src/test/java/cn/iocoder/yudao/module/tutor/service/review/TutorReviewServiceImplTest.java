package cn.iocoder.yudao.module.tutor.service.review;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.tutor.controller.app.review.vo.AppTutorReviewCreateReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.match.TutorMatchRecordDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.match.TutorMatchRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.review.TutorReviewMapper;
import cn.iocoder.yudao.module.tutor.enums.match.TutorMatchStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import cn.iocoder.yudao.module.tutor.service.point.TutorPointRewardService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

class TutorReviewServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorReviewServiceImpl reviewService;
    @Mock
    private TutorReviewMapper reviewMapper;
    @Mock
    private TutorMatchRecordMapper matchRecordMapper;
    @Mock
    private TutorTeacherResumeMapper resumeMapper;
    @Mock
    private TutorPointRewardService pointRewardService;
    @Mock
    private TutorNotifyService tutorNotifyService;

    @Test
    void createReview_whenFiveStars_rewardsReviewerAndTarget() {
        when(matchRecordMapper.selectById(10L)).thenReturn(TutorMatchRecordDO.builder()
                .id(10L).parentUserId(100L).teacherUserId(200L)
                .status(TutorMatchStatusEnum.BOTH_CONFIRMED.getStatus()).build());
        AppTutorReviewCreateReqVO reqVO = new AppTutorReviewCreateReqVO();
        reqVO.setMatchId(10L);
        reqVO.setRating(5);

        reviewService.createReview(100L, reqVO);

        verify(pointRewardService).reward(100L, TutorPointTaskTypeEnum.FIVE_STAR_REVIEW,
                "10:reviewer", "五星评价奖励");
        verify(pointRewardService).reward(200L, TutorPointTaskTypeEnum.FIVE_STAR_REVIEW,
                "10:target", "五星好评奖励");
        verify(tutorNotifyService, never()).sendPointChanged(anyLong(), anyString(), anyInt());
    }

    @Test
    void createReview_whenFourStars_doesNotRewardEitherSide() {
        when(matchRecordMapper.selectById(10L)).thenReturn(TutorMatchRecordDO.builder()
                .id(10L).parentUserId(100L).teacherUserId(200L)
                .status(TutorMatchStatusEnum.BOTH_CONFIRMED.getStatus()).build());
        AppTutorReviewCreateReqVO reqVO = new AppTutorReviewCreateReqVO();
        reqVO.setMatchId(10L);
        reqVO.setRating(4);

        reviewService.createReview(100L, reqVO);

        verifyNoInteractions(pointRewardService);
    }

}
