package cn.iocoder.yudao.module.tutor.controller.admin.review;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.review.vo.AdminTutorReviewPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.review.vo.AdminTutorReviewUpdateStatusReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.review.AppTutorReviewController;
import cn.iocoder.yudao.module.tutor.controller.app.review.vo.AppTutorReviewRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.review.TutorReviewDO;
import cn.iocoder.yudao.module.tutor.service.review.TutorReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 家教评价")
@RestController
@RequestMapping("/tutor/reviews")
@Validated
public class AdminTutorReviewController {

    @Resource
    private TutorReviewService reviewService;

    @GetMapping("/page")
    @Operation(summary = "获得评价分页")
    @PreAuthorize("@ss.hasPermission('tutor:review:query')")
    public CommonResult<PageResult<AppTutorReviewRespVO>> getReviewPage(@Valid AdminTutorReviewPageReqVO pageReqVO) {
        PageResult<TutorReviewDO> pageResult = reviewService.getReviewPage(pageReqVO);
        return success(new PageResult<>(pageResult.getList().stream()
                .map(AppTutorReviewController::convert)
                .collect(Collectors.toList()), pageResult.getTotal()));
    }

    @PutMapping("/status")
    @Operation(summary = "更新评价状态")
    @PreAuthorize("@ss.hasPermission('tutor:review:update-status')")
    public CommonResult<AppTutorReviewRespVO> updateReviewStatus(
            @RequestBody @Valid AdminTutorReviewUpdateStatusReqVO reqVO) {
        return success(AppTutorReviewController.convert(reviewService.updateReviewStatus(reqVO)));
    }

}
