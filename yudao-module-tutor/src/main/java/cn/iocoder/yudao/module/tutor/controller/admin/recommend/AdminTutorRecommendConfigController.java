package cn.iocoder.yudao.module.tutor.controller.admin.recommend;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 家教推荐权重配置")
@RestController
@RequestMapping("/tutor/recommend-config")
@Validated
public class AdminTutorRecommendConfigController {

    @GetMapping("/get")
    @Operation(summary = "获得推荐权重配置")
    @PreAuthorize("@ss.hasPermission('tutor:recommend:query')")
    public CommonResult<Map<String, Object>> getRecommendConfig(String scene, String cityCode, String abGroup) {
        Map<String, Object> config = new HashMap<>();
        config.put("scene", scene == null ? "square_resume" : scene);
        config.put("cityCode", cityCode);
        config.put("abGroup", abGroup == null ? "default" : abGroup);
        config.put("distanceWeight", 30);
        config.put("ratingWeight", 30);
        config.put("activeWeight", 20);
        config.put("topWeight", 20);
        return success(config);
    }

    @PutMapping("/save")
    @Operation(summary = "保存推荐权重配置")
    @PreAuthorize("@ss.hasPermission('tutor:recommend:update')")
    public CommonResult<Boolean> saveRecommendConfig(@RequestBody Map<String, Object> reqVO) {
        return success(true);
    }
}
