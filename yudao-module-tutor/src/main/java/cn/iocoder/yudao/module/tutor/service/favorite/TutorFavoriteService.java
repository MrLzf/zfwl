package cn.iocoder.yudao.module.tutor.service.favorite;

import cn.iocoder.yudao.module.tutor.controller.app.common.vo.AppTutorTargetReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.favorite.TutorFavoriteDO;

import java.util.List;

public interface TutorFavoriteService {
    TutorFavoriteDO favorite(Long userId, AppTutorTargetReqVO reqVO);
    void unfavorite(Long userId, AppTutorTargetReqVO reqVO);
    List<TutorFavoriteDO> getMyFavoriteList(Long userId);
}
