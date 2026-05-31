package cn.iocoder.yudao.module.tutor.controller.app.demand;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.demand.vo.AppTutorDemandRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.demand.vo.AppTutorDemandSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorTeachModeEnum;
import cn.iocoder.yudao.module.tutor.service.demand.TutorDemandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 家长需求")
@RestController
@RequestMapping("/tutor/demands")
@Validated
public class AppTutorDemandController {

    @Resource
    private TutorDemandService demandService;

    @PostMapping
    @Operation(summary = "发布家长需求")
    public CommonResult<AppTutorDemandRespVO> createDemand(@RequestBody @Valid AppTutorDemandSaveReqVO reqVO) {
        return success(convert(demandService.createDemand(getLoginUserId(), reqVO)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改家长需求")
    @Parameter(name = "id", description = "需求编号", required = true)
    public CommonResult<AppTutorDemandRespVO> updateDemand(@PathVariable("id") Long id,
                                                           @RequestBody @Valid AppTutorDemandSaveReqVO reqVO) {
        return success(convert(demandService.updateDemand(getLoginUserId(), id, reqVO)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "下架家长需求")
    @Parameter(name = "id", description = "需求编号", required = true)
    public CommonResult<Boolean> offlineDemand(@PathVariable("id") Long id) {
        demandService.offlineDemand(getLoginUserId(), id);
        return success(true);
    }

    @GetMapping("/my")
    @Operation(summary = "获得我的家长需求列表")
    public CommonResult<List<AppTutorDemandRespVO>> getMyDemandList() {
        return success(demandService.getMyDemandList(getLoginUserId()).stream()
                .map(AppTutorDemandController::convert)
                .collect(Collectors.toList()));
    }

    public static AppTutorDemandRespVO convert(TutorDemandDO demand) {
        if (demand == null) {
            return null;
        }
        return AppTutorDemandRespVO.builder()
                .id(demand.getId())
                .userId(demand.getUserId())
                .title(demand.getTitle())
                .grade(demand.getGrade())
                .subjects(demand.getSubjects())
                .teachMode(demand.getTeachMode())
                .teachModeName(getTeachModeName(demand.getTeachMode()))
                .address(demand.getAddress())
                .budgetMin(demand.getBudgetMin())
                .budgetMax(demand.getBudgetMax())
                .description(demand.getDescription())
                .cityCode(demand.getCityCode())
                .cityName(demand.getCityName())
                .longitude(demand.getLongitude())
                .latitude(demand.getLatitude())
                .distanceVisible(demand.getDistanceVisible())
                .contactMobileMask(demand.getContactMobileMask())
                .contactWechatMask(demand.getContactWechatMask())
                .status(demand.getStatus())
                .statusName(getPublishStatusName(demand.getStatus()))
                .auditStatus(demand.getAuditStatus())
                .auditStatusName(getAuditStatusName(demand.getAuditStatus()))
                .rejectReason(demand.getRejectReason())
                .viewCount(demand.getViewCount())
                .contactViewCount(demand.getContactViewCount())
                .matchCount(demand.getMatchCount())
                .expireTime(demand.getExpireTime())
                .createTime(demand.getCreateTime())
                .build();
    }

    private static String getTeachModeName(Integer teachMode) {
        for (TutorTeachModeEnum modeEnum : TutorTeachModeEnum.values()) {
            if (modeEnum.getMode().equals(teachMode)) {
                return modeEnum.getName();
            }
        }
        return null;
    }

    private static String getPublishStatusName(Integer status) {
        for (TutorPublishStatusEnum statusEnum : TutorPublishStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum.getName();
            }
        }
        return null;
    }

    private static String getAuditStatusName(Integer status) {
        for (TutorAuditStatusEnum statusEnum : TutorAuditStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum.getName();
            }
        }
        return null;
    }

}
