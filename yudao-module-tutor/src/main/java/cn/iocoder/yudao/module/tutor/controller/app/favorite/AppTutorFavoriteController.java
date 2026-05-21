package cn.iocoder.yudao.module.tutor.controller.app.favorite;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.common.vo.AppTutorTargetReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.favorite.vo.AppTutorFavoriteRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.favorite.TutorFavoriteDO;
import cn.iocoder.yudao.module.tutor.service.favorite.TutorFavoriteService;
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

@Tag(name = "用户 App - 家教收藏")
@RestController
@RequestMapping("/tutor/favorites")
@Validated
public class AppTutorFavoriteController {

    @Resource
    private TutorFavoriteService favoriteService;

    @PostMapping
    @Operation(summary = "收藏")
    public CommonResult<AppTutorFavoriteRespVO> favorite(@RequestBody @Valid AppTutorTargetReqVO reqVO) {
        return success(convert(favoriteService.favorite(getLoginUserId(), reqVO)));
    }

    @DeleteMapping
    @Operation(summary = "取消收藏")
    public CommonResult<Boolean> unfavorite(@RequestBody @Valid AppTutorTargetReqVO reqVO) {
        favoriteService.unfavorite(getLoginUserId(), reqVO);
        return success(true);
    }

    @GetMapping
    @Operation(summary = "获得我的收藏")
    public CommonResult<List<AppTutorFavoriteRespVO>> getMyFavoriteList() {
        return success(favoriteService.getMyFavoriteList(getLoginUserId()).stream()
                .map(AppTutorFavoriteController::convert).collect(Collectors.toList()));
    }

    private static AppTutorFavoriteRespVO convert(TutorFavoriteDO favorite) {
        return AppTutorFavoriteRespVO.builder()
                .id(favorite.getId()).targetType(favorite.getTargetType()).targetId(favorite.getTargetId())
                .targetUserId(favorite.getTargetUserId()).title(favorite.getTitle())
                .cityCode(favorite.getCityCode()).cityName(favorite.getCityName())
                .createTime(favorite.getCreateTime()).build();
    }

}
