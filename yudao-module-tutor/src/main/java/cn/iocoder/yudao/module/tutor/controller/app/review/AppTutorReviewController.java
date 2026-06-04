package cn.iocoder.yudao.module.tutor.controller.app.review;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.review.vo.AppTutorReviewCreateReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.review.vo.AppTutorReviewRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.review.TutorReviewDO;
import cn.iocoder.yudao.module.tutor.service.review.TutorReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 家教评价")
@RestController
@RequestMapping("/tutor/reviews")
@Validated
public class AppTutorReviewController {

    @Resource
    private TutorReviewService reviewService;

    @PostMapping
    @Operation(summary = "提交评价")
    public CommonResult<AppTutorReviewRespVO> createReview(@RequestBody @Valid AppTutorReviewCreateReqVO reqVO) {
        return success(convert(reviewService.createReview(getLoginUserId(), reqVO)));
    }

    @GetMapping("/my")
    @Operation(summary = "获得我的评价")
    public CommonResult<List<AppTutorReviewRespVO>> getMyReviewList() {
        return success(reviewService.getMyReviewList(getLoginUserId()).stream()
                .map(AppTutorReviewController::convert).collect(Collectors.toList()));
    }

    @GetMapping("/target")
    @Operation(summary = "获得目标用户评价")
    public CommonResult<List<AppTutorReviewRespVO>> getTargetReviewList(@RequestParam("targetUserId") @NotNull Long targetUserId) {
        return success(reviewService.getTargetReviewList(targetUserId).stream()
                .map(AppTutorReviewController::convert).collect(Collectors.toList()));
    }

    @GetMapping("/target/tags")
    @Operation(summary = "获得目标用户评价标签统计")
    public CommonResult<Map<String, Long>> getTargetReviewTagStats(@RequestParam("targetUserId") @NotNull Long targetUserId) {
        return success(reviewService.getTargetReviewTagStats(targetUserId));
    }

    public static AppTutorReviewRespVO convert(TutorReviewDO review) {
        return AppTutorReviewRespVO.builder()
                .id(review.getId()).matchId(review.getMatchId()).reviewerUserId(review.getReviewerUserId())
                .targetUserId(review.getTargetUserId()).rating(review.getRating()).tags(review.getTags())
                .content(review.getContent()).anonymousDisplay(review.getAnonymousDisplay())
                .status(review.getStatus()).createTime(review.getCreateTime()).build();
    }

}
