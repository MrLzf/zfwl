package cn.iocoder.yudao.module.tutor.controller.app.city;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.city.vo.AppTutorCityRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import cn.iocoder.yudao.module.tutor.service.city.TutorCityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - 家教城市")
@RestController
@RequestMapping("/tutor/cities")
@Validated
public class AppTutorCityController {

    @Resource
    private TutorCityService cityService;

    @GetMapping
    @Operation(summary = "获得家教城市列表")
    @PermitAll
    public CommonResult<List<AppTutorCityRespVO>> getCityList() {
        List<TutorCityDO> cities = cityService.getEnabledCityList();
        return success(cities.stream().map(AppTutorCityRespVO::of).collect(Collectors.toList()));
    }

}
