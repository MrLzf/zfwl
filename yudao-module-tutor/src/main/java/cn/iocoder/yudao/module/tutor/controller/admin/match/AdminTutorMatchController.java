package cn.iocoder.yudao.module.tutor.controller.admin.match;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.match.vo.AdminTutorMatchPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.match.AppTutorMatchController;
import cn.iocoder.yudao.module.tutor.controller.app.match.vo.AppTutorMatchRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.match.TutorMatchRecordDO;
import cn.iocoder.yudao.module.tutor.service.match.TutorMatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 家教匹配记录")
@RestController
@RequestMapping("/tutor/matches")
@Validated
public class AdminTutorMatchController {

    @Resource
    private TutorMatchService matchService;

    @GetMapping("/page")
    @Operation(summary = "获得匹配记录分页")
    @PreAuthorize("@ss.hasPermission('tutor:match:query')")
    public CommonResult<PageResult<AppTutorMatchRespVO>> getMatchPage(@Valid AdminTutorMatchPageReqVO pageReqVO) {
        PageResult<TutorMatchRecordDO> pageResult = matchService.getMatchPage(pageReqVO);
        return success(new PageResult<>(pageResult.getList().stream()
                .map(AppTutorMatchController::convert)
                .collect(Collectors.toList()), pageResult.getTotal()));
    }

}
