package cn.iocoder.yudao.module.tutor.controller.admin.city;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.admin.city.vo.AdminTutorCityRespVO;
import cn.iocoder.yudao.module.tutor.controller.admin.city.vo.AdminTutorCityUpdateReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import cn.iocoder.yudao.module.tutor.service.city.TutorCityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 家教城市")
@RestController
@RequestMapping("/tutor/city")
@Validated
public class AdminTutorCityController {

    @Resource
    private TutorCityService cityService;

    @GetMapping("/list")
    @Operation(summary = "获得家教城市列表")
    @PreAuthorize("@ss.hasPermission('tutor:city:query')")
    public CommonResult<List<AdminTutorCityRespVO>> getCityList() {
        List<TutorCityDO> cities = cityService.getCityList();
        return success(cities.stream().map(AdminTutorCityRespVO::of).collect(Collectors.toList()));
    }

    @PutMapping("/update")
    @Operation(summary = "更新家教城市运营状态")
    @PreAuthorize("@ss.hasPermission('tutor:city:update')")
    public CommonResult<Boolean> updateCity(@Valid @RequestBody AdminTutorCityUpdateReqVO updateReqVO) {
        cityService.updateCity(updateReqVO.getId(), updateReqVO.getOpened(), updateReqVO.getHot(),
                updateReqVO.getSort(), updateReqVO.getStatus());
        return success(true);
    }

}
