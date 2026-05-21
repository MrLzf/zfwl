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
        return selectPage(reqVO, new LambdaQueryWrapperX<TutorDemandDO>()
                .eq(TutorDemandDO::getStatus, TutorPublishStatusEnum.SHOWING.getStatus())
                .eq(TutorDemandDO::getAuditStatus, TutorAuditStatusEnum.APPROVED.getStatus())
                .gtIfPresent(TutorDemandDO::getExpireTime, LocalDateTime.now())
                .eqIfPresent(TutorDemandDO::getCityCode, reqVO.getCityCode())
                .likeIfPresent(TutorDemandDO::getSubjects, reqVO.getSubject())
                .likeIfPresent(TutorDemandDO::getGrade, reqVO.getGrade())
                .eqIfPresent(TutorDemandDO::getTeachMode, reqVO.getTeachMode())
                .geIfPresent(TutorDemandDO::getBudgetMax, reqVO.getPriceMin())
                .leIfPresent(TutorDemandDO::getBudgetMin, reqVO.getPriceMax())
                .orderByDesc(TutorDemandDO::getTopUntil)
                .orderByDesc(TutorDemandDO::getUrgentUntil)
                .orderByDesc(TutorDemandDO::getId));
    }

    default void updateViewCountIncr(Long id) {
        update(null, new LambdaUpdateWrapper<TutorDemandDO>()
                .eq(TutorDemandDO::getId, id)
                .setSql("view_count = view_count + 1"));
    }

}
