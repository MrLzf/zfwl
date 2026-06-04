package cn.iocoder.yudao.module.tutor.controller.app.complaint;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.complaint.vo.AppTutorComplaintCreateReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.complaint.vo.AppTutorComplaintRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.complaint.TutorComplaintDO;
import cn.iocoder.yudao.module.tutor.service.complaint.TutorComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 家教举报投诉")
@RestController
@RequestMapping("/tutor/complaints")
@Validated
public class AppTutorComplaintController {

    @Resource
    private TutorComplaintService complaintService;

    @PostMapping
    @Operation(summary = "提交举报")
    public CommonResult<AppTutorComplaintRespVO> createComplaint(@RequestBody @Valid AppTutorComplaintCreateReqVO reqVO) {
        return success(convert(complaintService.createComplaint(getLoginUserId(), reqVO)));
    }

    @GetMapping("/my")
    @Operation(summary = "我的举报")
    public CommonResult<List<AppTutorComplaintRespVO>> getMyComplaintList() {
        return success(complaintService.getMyComplaintList(getLoginUserId()).stream()
                .map(AppTutorComplaintController::convert).collect(Collectors.toList()));
    }

    public static AppTutorComplaintRespVO convert(TutorComplaintDO complaint) {
        return AppTutorComplaintRespVO.builder().id(complaint.getId()).targetType(complaint.getTargetType())
                .targetId(complaint.getTargetId()).reasonType(complaint.getReasonType()).content(complaint.getContent())
                .imageUrls(complaint.getImageUrls()).status(complaint.getStatus())
                .handleResult(complaint.getHandleResult()).createTime(complaint.getCreateTime()).build();
    }
}
