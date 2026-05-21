package cn.iocoder.yudao.module.tutor.controller.app.detail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.demand.AppTutorDemandController;
import cn.iocoder.yudao.module.tutor.controller.app.demand.vo.AppTutorDemandRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.detail.vo.AppTutorDetailRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.resume.AppTutorTeacherResumeController;
import cn.iocoder.yudao.module.tutor.controller.app.resume.vo.AppTutorTeacherResumeRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.square.AppTutorSquareController;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.enums.target.TutorTargetTypeEnum;
import cn.iocoder.yudao.module.tutor.service.demand.TutorDemandService;
import cn.iocoder.yudao.module.tutor.service.history.TutorBrowseHistoryService;
import cn.iocoder.yudao.module.tutor.service.resume.TutorTeacherResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.CONTACT_TARGET_NOT_EXISTS;

@Tag(name = "用户 App - 家教详情")
@RestController
@RequestMapping("/tutor/detail")
@Validated
public class AppTutorDetailController {

    private static final String SAFETY_TIP = "请优先通过平台确认对方身份，线下见面建议选择公共场所，未确认前不要提前转账。";

    @Resource
    private TutorDemandService demandService;
    @Resource
    private TutorTeacherResumeService resumeService;
    @Resource
    private TutorBrowseHistoryService browseHistoryService;

    @GetMapping("/{targetType}/{id}")
    @Operation(summary = "获得家教详情")
    @Parameter(name = "targetType", description = "目标类型：demand 家长需求，resume 教师简历", required = true)
    @Parameter(name = "id", description = "目标编号", required = true)
    public CommonResult<AppTutorDetailRespVO> getDetail(@PathVariable("targetType") String targetType,
                                                        @PathVariable("id") Long id,
                                                        BigDecimal longitude,
                                                        BigDecimal latitude) {
        if (TutorTargetTypeEnum.isDemand(targetType)) {
            return success(getDemandDetail(id, longitude, latitude));
        }
        if (TutorTargetTypeEnum.isResume(targetType)) {
            return success(getResumeDetail(id, longitude, latitude));
        }
        throw exception(CONTACT_TARGET_NOT_EXISTS);
    }

    private AppTutorDetailRespVO getDemandDetail(Long id, BigDecimal longitude, BigDecimal latitude) {
        TutorDemandDO demand = demandService.viewSquareDemand(id);
        browseHistoryService.recordBrowseHistory(getLoginUserId(), TutorTargetTypeEnum.DEMAND.getType(), demand.getId(),
                demand.getUserId(), demand.getTitle(), demand.getCityCode(), demand.getCityName());
        AppTutorDemandRespVO demandRespVO = AppTutorDemandController.convert(demand);
        demandRespVO.setDistanceKm(AppTutorSquareController.calculateDistance(longitude, latitude,
                demand.getLongitude(), demand.getLatitude()));
        return AppTutorDetailRespVO.builder()
                .targetType(TutorTargetTypeEnum.DEMAND.getType())
                .demand(demandRespVO)
                .contactUnlocked(false)
                .safetyTip(SAFETY_TIP)
                .build();
    }

    private AppTutorDetailRespVO getResumeDetail(Long id, BigDecimal longitude, BigDecimal latitude) {
        TutorTeacherResumeDO resume = resumeService.viewSquareResume(id);
        browseHistoryService.recordBrowseHistory(getLoginUserId(), TutorTargetTypeEnum.RESUME.getType(), resume.getId(),
                resume.getUserId(), resume.getTitle(), resume.getCityCode(), resume.getCityName());
        AppTutorTeacherResumeRespVO resumeRespVO = AppTutorTeacherResumeController.convert(resume);
        resumeRespVO.setDistanceKm(AppTutorSquareController.calculateDistance(longitude, latitude,
                resume.getLongitude(), resume.getLatitude()));
        return AppTutorDetailRespVO.builder()
                .targetType(TutorTargetTypeEnum.RESUME.getType())
                .resume(resumeRespVO)
                .contactUnlocked(false)
                .safetyTip(SAFETY_TIP)
                .build();
    }

}
