package cn.iocoder.yudao.module.tutor.dal.mysql.demand;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.controller.admin.demand.vo.AdminTutorDemandPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.square.vo.AppTutorDemandPageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Mapper
public interface TutorDemandMapper extends BaseMapperX<TutorDemandDO> {

    default List<TutorDemandDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<TutorDemandDO>()
                .eq(TutorDemandDO::getUserId, userId)
                .orderByDesc(TutorDemandDO::getId));
    }

    default Long selectCountByUserIdAndStatuses(Long userId, Collection<Integer> statuses) {
        return selectCount(new LambdaQueryWrapperX<TutorDemandDO>()
                .eq(TutorDemandDO::getUserId, userId)
                .in(TutorDemandDO::getStatus, statuses));
    }

    default PageResult<TutorDemandDO> selectPage(AdminTutorDemandPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TutorDemandDO>()
                .eqIfPresent(TutorDemandDO::getUserId, reqVO.getUserId())
                .likeIfPresent(TutorDemandDO::getTitle, reqVO.getTitle())
                .eqIfPresent(TutorDemandDO::getCityCode, reqVO.getCityCode())
                .likeIfPresent(TutorDemandDO::getSubjects, reqVO.getSubject())
                .likeIfPresent(TutorDemandDO::getGrade, reqVO.getGrade())
                .eqIfPresent(TutorDemandDO::getTeachMode, reqVO.getTeachMode())
                .eqIfPresent(TutorDemandDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TutorDemandDO::getAuditStatus, reqVO.getAuditStatus())
                .betweenIfPresent(TutorDemandDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TutorDemandDO::getId));
    }

    default PageResult<TutorDemandDO> selectSquarePage(AppTutorDemandPageReqVO reqVO) {
        QueryWrapper<TutorDemandDO> wrapper = new QueryWrapper<TutorDemandDO>()
                .eq("status", TutorPublishStatusEnum.SHOWING.getStatus())
                .eq("audit_status", TutorAuditStatusEnum.APPROVED.getStatus())
                .gt("expire_time", LocalDateTime.now())
                .eq(reqVO.getCityCode() != null, "city_code", reqVO.getCityCode())
                .like(reqVO.getSubject() != null, "subjects", reqVO.getSubject())
                .like(reqVO.getGrade() != null, "grade", reqVO.getGrade())
                .eq(reqVO.getTeachMode() != null, "teach_mode", reqVO.getTeachMode())
                .ge(reqVO.getPriceMin() != null, "budget_max", reqVO.getPriceMin())
                .le(reqVO.getPriceMax() != null, "budget_min", reqVO.getPriceMax());
        appendDistanceConditionAndOrder(wrapper, reqVO.getLongitude(), reqVO.getLatitude(), reqVO.getDistanceKm(),
                reqVO.getSortType());
        return selectPage(reqVO, wrapper);
    }

    default void updateViewCountIncr(Long id) {
        update(null, new LambdaUpdateWrapper<TutorDemandDO>()
                .eq(TutorDemandDO::getId, id)
                .setSql("view_count = view_count + 1"));
    }

    default void updateContactViewCountIncr(Long id) {
        update(null, new LambdaUpdateWrapper<TutorDemandDO>()
                .eq(TutorDemandDO::getId, id)
                .setSql("contact_view_count = contact_view_count + 1"));
    }

    default void updateMatchCountIncr(Long id) {
        update(null, new LambdaUpdateWrapper<TutorDemandDO>()
                .eq(TutorDemandDO::getId, id)
                .setSql("match_count = match_count + 1"));
    }

    static void appendDistanceConditionAndOrder(QueryWrapper<?> wrapper, java.math.BigDecimal longitude,
                                                java.math.BigDecimal latitude, Integer distanceKm, String sortType) {
        String distanceExpr = buildDistanceExpr(longitude, latitude);
        if (distanceExpr != null && distanceKm != null) {
            wrapper.isNotNull("longitude").isNotNull("latitude")
                    .ne("longitude", 0).ne("latitude", 0)
                    .apply(distanceExpr + " <= {0}", distanceKm);
        }
        if ("distance".equals(sortType) && distanceExpr != null) {
            wrapper.orderByAsc(distanceExpr);
        } else if ("latest".equals(sortType)) {
            wrapper.orderByDesc("id");
            return;
        }
        wrapper.orderByDesc("top_until").orderByDesc("urgent_until").orderByDesc("id");
    }

    static String buildDistanceExpr(java.math.BigDecimal longitude, java.math.BigDecimal latitude) {
        if (longitude == null || latitude == null) {
            return null;
        }
        String lng = longitude.toPlainString();
        String lat = latitude.toPlainString();
        return "ROUND(6371 * 2 * ASIN(SQRT(POW(SIN((RADIANS(latitude) - RADIANS(" + lat + ")) / 2), 2) "
                + "+ COS(RADIANS(" + lat + ")) * COS(RADIANS(latitude)) "
                + "* POW(SIN((RADIANS(longitude) - RADIANS(" + lng + ")) / 2), 2))), 1)";
    }

}
