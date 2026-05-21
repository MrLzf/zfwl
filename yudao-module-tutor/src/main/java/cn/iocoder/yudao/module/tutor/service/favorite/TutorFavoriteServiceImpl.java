package cn.iocoder.yudao.module.tutor.service.favorite;

import cn.iocoder.yudao.module.tutor.controller.app.common.vo.AppTutorTargetReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.favorite.TutorFavoriteDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.favorite.TutorFavoriteMapper;
import cn.iocoder.yudao.module.tutor.enums.target.TutorTargetTypeEnum;
import cn.iocoder.yudao.module.tutor.service.demand.TutorDemandService;
import cn.iocoder.yudao.module.tutor.service.resume.TutorTeacherResumeService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.CONTACT_TARGET_NOT_EXISTS;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.FAVORITE_COUNT_EXCEED;

@Service
@Validated
public class TutorFavoriteServiceImpl implements TutorFavoriteService {

    private static final int MAX_FAVORITE_COUNT = 100;

    @Resource
    private TutorFavoriteMapper favoriteMapper;
    @Resource
    private TutorDemandService demandService;
    @Resource
    private TutorTeacherResumeService resumeService;

    @Override
    public TutorFavoriteDO favorite(Long userId, AppTutorTargetReqVO reqVO) {
        TutorFavoriteDO favorite = favoriteMapper.selectByUserAndTarget(userId, reqVO.getTargetType(), reqVO.getTargetId());
        if (favorite != null) {
            return favorite;
        }
        if (favoriteMapper.selectCountByUserId(userId) >= MAX_FAVORITE_COUNT) {
            throw exception(FAVORITE_COUNT_EXCEED);
        }
        favorite = buildFavorite(userId, reqVO);
        favoriteMapper.insert(favorite);
        return favorite;
    }

    @Override
    public void unfavorite(Long userId, AppTutorTargetReqVO reqVO) {
        TutorFavoriteDO favorite = favoriteMapper.selectByUserAndTarget(userId, reqVO.getTargetType(), reqVO.getTargetId());
        if (favorite != null) {
            favoriteMapper.deleteById(favorite.getId());
        }
    }

    @Override
    public List<TutorFavoriteDO> getMyFavoriteList(Long userId) {
        return favoriteMapper.selectListByUserId(userId);
    }

    private TutorFavoriteDO buildFavorite(Long userId, AppTutorTargetReqVO reqVO) {
        if (TutorTargetTypeEnum.isDemand(reqVO.getTargetType())) {
            TutorDemandDO demand = demandService.getSquareDemand(reqVO.getTargetId());
            return TutorFavoriteDO.builder().userId(userId).targetType(reqVO.getTargetType())
                    .targetId(demand.getId()).targetUserId(demand.getUserId()).title(demand.getTitle())
                    .cityCode(demand.getCityCode()).cityName(demand.getCityName()).build();
        }
        if (TutorTargetTypeEnum.isResume(reqVO.getTargetType())) {
            TutorTeacherResumeDO resume = resumeService.getSquareResume(reqVO.getTargetId());
            return TutorFavoriteDO.builder().userId(userId).targetType(reqVO.getTargetType())
                    .targetId(resume.getId()).targetUserId(resume.getUserId()).title(resume.getTitle())
                    .cityCode(resume.getCityCode()).cityName(resume.getCityName()).build();
        }
        throw exception(CONTACT_TARGET_NOT_EXISTS);
    }

}
