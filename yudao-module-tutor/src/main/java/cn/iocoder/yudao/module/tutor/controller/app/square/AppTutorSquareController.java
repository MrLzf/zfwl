package cn.iocoder.yudao.module.tutor.controller.app.square;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.app.demand.AppTutorDemandController;
import cn.iocoder.yudao.module.tutor.controller.app.demand.vo.AppTutorDemandRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.resume.AppTutorTeacherResumeController;
import cn.iocoder.yudao.module.tutor.controller.app.resume.vo.AppTutorTeacherResumeRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.square.vo.AppTutorDemandPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.square.vo.AppTutorTeacherResumePageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.service.demand.TutorDemandService;
import cn.iocoder.yudao.module.tutor.service.resume.TutorTeacherResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - 家教广场")
@RestController
@RequestMapping("/tutor/square")
@Validated
public class AppTutorSquareController {

    @Resource
    private TutorDemandService demandService;
    @Resource
    private TutorTeacherResumeService resumeService;

    @GetMapping("/demands")
    @Operation(summary = "获得需求广场分页")
    public CommonResult<PageResult<AppTutorDemandRespVO>> getDemandPage(@Valid AppTutorDemandPageReqVO pageReqVO) {
        PageResult<TutorDemandDO> pageResult = demandService.getSquareDemandPage(pageReqVO);
        List<AppTutorDemandRespVO> list = pageResult.getList().stream()
                .map(demand -> {
                    AppTutorDemandRespVO respVO = AppTutorDemandController.convert(demand);
                    respVO.setDistanceKm(calculateDistance(pageReqVO.getLongitude(), pageReqVO.getLatitude(),
                            demand.getLongitude(), demand.getLatitude()));
                    return respVO;
                })
                .collect(Collectors.toList());
        return success(new PageResult<>(list, pageResult.getTotal()));
    }

    @GetMapping("/demands/{id}")
    @Operation(summary = "获得需求详情")
    @Parameter(name = "id", description = "需求编号", required = true)
    public CommonResult<AppTutorDemandRespVO> getDemand(@PathVariable("id") Long id,
                                                        BigDecimal longitude,
                                                        BigDecimal latitude) {
        TutorDemandDO demand = demandService.viewSquareDemand(id);
        AppTutorDemandRespVO respVO = AppTutorDemandController.convert(demand);
        respVO.setDistanceKm(calculateDistance(longitude, latitude, demand.getLongitude(), demand.getLatitude()));
        return success(respVO);
    }

    @GetMapping("/resumes")
    @Operation(summary = "获得教师广场分页")
    public CommonResult<PageResult<AppTutorTeacherResumeRespVO>> getResumePage(
            @Valid AppTutorTeacherResumePageReqVO pageReqVO) {
        PageResult<TutorTeacherResumeDO> pageResult = resumeService.getSquareResumePage(pageReqVO);
        List<AppTutorTeacherResumeRespVO> list = pageResult.getList().stream()
                .map(resume -> {
                    AppTutorTeacherResumeRespVO respVO = AppTutorTeacherResumeController.convert(resume);
                    respVO.setDistanceKm(calculateDistance(pageReqVO.getLongitude(), pageReqVO.getLatitude(),
                            resume.getLongitude(), resume.getLatitude()));
                    return respVO;
                })
                .collect(Collectors.toList());
        return success(new PageResult<>(list, pageResult.getTotal()));
    }

    @GetMapping("/resumes/{id}")
    @Operation(summary = "获得教师详情")
    @Parameter(name = "id", description = "简历编号", required = true)
    public CommonResult<AppTutorTeacherResumeRespVO> getResume(@PathVariable("id") Long id,
                                                               BigDecimal longitude,
                                                               BigDecimal latitude) {
        TutorTeacherResumeDO resume = resumeService.viewSquareResume(id);
        AppTutorTeacherResumeRespVO respVO = AppTutorTeacherResumeController.convert(resume);
        respVO.setDistanceKm(calculateDistance(longitude, latitude, resume.getLongitude(), resume.getLatitude()));
        return success(respVO);
    }

    public static BigDecimal calculateDistance(BigDecimal fromLongitude, BigDecimal fromLatitude,
                                               BigDecimal toLongitude, BigDecimal toLatitude) {
        if (fromLongitude == null || fromLatitude == null || toLongitude == null || toLatitude == null
                || Objects.equals(BigDecimal.ZERO, toLongitude) || Objects.equals(BigDecimal.ZERO, toLatitude)) {
            return null;
        }
        double fromLng = Math.toRadians(fromLongitude.doubleValue());
        double fromLat = Math.toRadians(fromLatitude.doubleValue());
        double toLng = Math.toRadians(toLongitude.doubleValue());
        double toLat = Math.toRadians(toLatitude.doubleValue());
        double haversine = Math.pow(Math.sin((toLat - fromLat) / 2), 2)
                + Math.cos(fromLat) * Math.cos(toLat) * Math.pow(Math.sin((toLng - fromLng) / 2), 2);
        double distance = 2 * 6371.0 * Math.asin(Math.sqrt(haversine));
        return BigDecimal.valueOf(distance).setScale(1, RoundingMode.HALF_UP);
    }

}
