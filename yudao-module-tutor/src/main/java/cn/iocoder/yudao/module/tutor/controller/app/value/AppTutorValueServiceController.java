package cn.iocoder.yudao.module.tutor.controller.app.value;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.value.vo.AppTutorValueServiceBuyReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.value.vo.AppTutorValueServiceConfigRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.value.TutorValueServiceConfigDO;
import cn.iocoder.yudao.module.tutor.service.value.TutorValueServiceService;
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

@Tag(name = "用户 App - 家教增值服务")
@RestController
@RequestMapping("/tutor/value-services")
@Validated
public class AppTutorValueServiceController {

    @Resource
    private TutorValueServiceService valueService;

    @GetMapping
    @Operation(summary = "获得增值服务配置")
    public CommonResult<List<AppTutorValueServiceConfigRespVO>> getConfigs(String targetType) {
        return success(valueService.getEnabledConfigList(targetType).stream().map(this::convert).collect(Collectors.toList()));
    }

    @PostMapping("/orders")
    @Operation(summary = "购买增值服务")
    public CommonResult<Boolean> buyService(@Valid @RequestBody AppTutorValueServiceBuyReqVO reqVO) {
        valueService.buyService(getLoginUserId(), reqVO);
        return success(true);
    }

    private AppTutorValueServiceConfigRespVO convert(TutorValueServiceConfigDO config) {
        return AppTutorValueServiceConfigRespVO.builder().id(config.getId()).serviceType(config.getServiceType())
                .targetType(config.getTargetType()).pointPrice(config.getPointPrice())
                .durationHours(config.getDurationHours()).build();
    }
}
