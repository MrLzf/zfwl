package cn.iocoder.yudao.module.tutor.controller.admin.complaint;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.complaint.vo.AdminTutorComplaintHandleReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.complaint.vo.AdminTutorComplaintPageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.complaint.TutorComplaintDO;
import cn.iocoder.yudao.module.tutor.service.complaint.TutorComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 家教举报投诉")
@RestController
@RequestMapping("/tutor/complaints")
@Validated
public class AdminTutorComplaintController {

    @Resource
    private TutorComplaintService complaintService;

    @GetMapping("/page")
    @Operation(summary = "举报分页")
    @PreAuthorize("@ss.hasPermission('tutor:complaint:query')")
    public CommonResult<PageResult<TutorComplaintDO>> getComplaintPage(@Valid AdminTutorComplaintPageReqVO reqVO) {
        return success(complaintService.getComplaintPage(reqVO));
    }

    @PutMapping("/handle")
    @Operation(summary = "处理举报")
    @PreAuthorize("@ss.hasPermission('tutor:complaint:handle')")
    public CommonResult<TutorComplaintDO> handleComplaint(@RequestBody @Valid AdminTutorComplaintHandleReqVO reqVO) {
        return success(complaintService.handleComplaint(getLoginUserId(), reqVO));
    }
}
