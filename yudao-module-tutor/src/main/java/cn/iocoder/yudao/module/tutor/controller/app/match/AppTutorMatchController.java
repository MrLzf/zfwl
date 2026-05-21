package cn.iocoder.yudao.module.tutor.controller.app.match;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.match.vo.AppTutorMatchRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.match.TutorMatchRecordDO;
import cn.iocoder.yudao.module.tutor.enums.match.TutorMatchStatusEnum;
import cn.iocoder.yudao.module.tutor.service.match.TutorMatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 匹配")
@RestController
@RequestMapping("/tutor/matches")
@Validated
public class AppTutorMatchController {

    @Resource
    private TutorMatchService matchService;

    @PostMapping("/{id}/confirm")
    @Operation(summary = "确认匹配")
    @Parameter(name = "id", description = "匹配编号", required = true)
    public CommonResult<AppTutorMatchRespVO> confirmMatch(@PathVariable("id") Long id) {
        return success(convert(matchService.confirmMatch(getLoginUserId(), id)));
    }

    @GetMapping("/my")
    @Operation(summary = "获得我的匹配记录")
    public CommonResult<List<AppTutorMatchRespVO>> getMyMatchList() {
        return success(matchService.getMyMatchList(getLoginUserId()).stream()
                .map(AppTutorMatchController::convert).collect(Collectors.toList()));
    }

    public static AppTutorMatchRespVO convert(TutorMatchRecordDO match) {
        return AppTutorMatchRespVO.builder()
                .id(match.getId()).demandId(match.getDemandId()).resumeId(match.getResumeId())
                .parentUserId(match.getParentUserId()).teacherUserId(match.getTeacherUserId())
                .source(match.getSource()).status(match.getStatus()).statusName(getStatusName(match.getStatus()))
                .parentConfirmTime(match.getParentConfirmTime()).teacherConfirmTime(match.getTeacherConfirmTime())
                .matchedTime(match.getMatchedTime()).createTime(match.getCreateTime()).build();
    }

    private static String getStatusName(Integer status) {
        for (TutorMatchStatusEnum statusEnum : TutorMatchStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum.getName();
            }
        }
        return null;
    }

}
