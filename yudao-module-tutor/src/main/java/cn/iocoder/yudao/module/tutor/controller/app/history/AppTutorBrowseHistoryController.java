package cn.iocoder.yudao.module.tutor.controller.app.history;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.history.vo.AppTutorBrowseHistoryRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.history.TutorBrowseHistoryDO;
import cn.iocoder.yudao.module.tutor.service.history.TutorBrowseHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 浏览历史")
@RestController
@RequestMapping("/tutor/browse-history")
@Validated
public class AppTutorBrowseHistoryController {

    @Resource
    private TutorBrowseHistoryService browseHistoryService;

    @GetMapping("/my")
    @Operation(summary = "获得我的浏览历史")
    public CommonResult<List<AppTutorBrowseHistoryRespVO>> getMyBrowseHistoryList() {
        return success(browseHistoryService.getMyBrowseHistoryList(getLoginUserId()).stream()
                .map(AppTutorBrowseHistoryController::convert)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除我的单条浏览历史")
    public CommonResult<Boolean> deleteMyBrowseHistory(@PathVariable("id") Long id) {
        browseHistoryService.deleteMyBrowseHistory(getLoginUserId(), id);
        return success(true);
    }

    @DeleteMapping("/my")
    @Operation(summary = "清空我的浏览历史")
    public CommonResult<Boolean> clearMyBrowseHistory() {
        browseHistoryService.clearMyBrowseHistory(getLoginUserId());
        return success(true);
    }

    private static AppTutorBrowseHistoryRespVO convert(TutorBrowseHistoryDO history) {
        return AppTutorBrowseHistoryRespVO.builder()
                .id(history.getId())
                .targetType(history.getTargetType())
                .targetId(history.getTargetId())
                .targetUserId(history.getTargetUserId())
                .title(history.getTitle())
                .cityCode(history.getCityCode())
                .cityName(history.getCityName())
                .createTime(history.getCreateTime())
                .build();
    }

}
