package cn.iocoder.yudao.module.tutor.dal.mysql.resume;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.controller.admin.resume.vo.AdminTutorTeacherResumePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.square.vo.AppTutorTeacherResumePageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface TutorTeacherResumeMapper extends BaseMapperX<TutorTeacherResumeDO> {

    default List<TutorTeacherResumeDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<TutorTeacherResumeDO>()
                .eq(TutorTeacherResumeDO::getUserId, userId)
                .orderByDesc(TutorTeacherResumeDO::getId));
    }

    default Long selectCountByUserIdAndStatuses(Long userId, Collection<Integer> statuses) {
        return selectCount(new LambdaQueryWrapperX<TutorTeacherResumeDO>()
                .eq(TutorTeacherResumeDO::getUserId, userId)
                .in(TutorTeacherResumeDO::getStatus, statuses));
    }

    default PageResult<TutorTeacherResumeDO> selectPage(AdminTutorTeacherResumePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TutorTeacherResumeDO>()
                .eqIfPresent(TutorTeacherResumeDO::getUserId, reqVO.getUserId())
                .likeIfPresent(TutorTeacherResumeDO::getTitle, reqVO.getTitle())
                .eqIfPresent(TutorTeacherResumeDO::getCityCode, reqVO.getCityCode())
                .likeIfPresent(TutorTeacherResumeDO::getSubjects, reqVO.getSubject())
                .likeIfPresent(TutorTeacherResumeDO::getTeachModes, reqVO.getTeachMode() == null ? null : reqVO.getTeachMode().toString())
                .eqIfPresent(TutorTeacherResumeDO::getFreeTrialEnabled, reqVO.getFreeTrialEnabled())
                .eqIfPresent(TutorTeacherResumeDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TutorTeacherResumeDO::getAuditStatus, reqVO.getAuditStatus())
                .betweenIfPresent(TutorTeacherResumeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TutorTeacherResumeDO::getId));
    }

    default PageResult<TutorTeacherResumeDO> selectSquarePage(AppTutorTeacherResumePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TutorTeacherResumeDO>()
                .eq(TutorTeacherResumeDO::getStatus, TutorPublishStatusEnum.SHOWING.getStatus())
                .eq(TutorTeacherResumeDO::getAuditStatus, TutorAuditStatusEnum.APPROVED.getStatus())
                .eqIfPresent(TutorTeacherResumeDO::getCityCode, reqVO.getCityCode())
                .likeIfPresent(TutorTeacherResumeDO::getSubjects, reqVO.getSubject())
                .likeIfPresent(TutorTeacherResumeDO::getTeachModes, reqVO.getTeachMode() == null ? null : reqVO.getTeachMode().toString())
                .geIfPresent(TutorTeacherResumeDO::getHourlyPrice, reqVO.getPriceMin())
                .leIfPresent(TutorTeacherResumeDO::getHourlyPrice, reqVO.getPriceMax())
                .eqIfPresent(TutorTeacherResumeDO::getFreeTrialEnabled, reqVO.getFreeTrialEnabled())
                .orderByDesc(TutorTeacherResumeDO::getTopUntil)
                .orderByDesc(TutorTeacherResumeDO::getUrgentUntil)
                .orderByDesc(TutorTeacherResumeDO::getId));
    }

    default void updateViewCountIncr(Long id) {
        update(null, new LambdaUpdateWrapper<TutorTeacherResumeDO>()
                .eq(TutorTeacherResumeDO::getId, id)
                .setSql("view_count = view_count + 1"));
    }

    default void updateContactViewCountIncr(Long id) {
        update(null, new LambdaUpdateWrapper<TutorTeacherResumeDO>()
                .eq(TutorTeacherResumeDO::getId, id)
                .setSql("contact_view_count = contact_view_count + 1"));
    }

    default void updateMatchCountIncr(Long id) {
        update(null, new LambdaUpdateWrapper<TutorTeacherResumeDO>()
                .eq(TutorTeacherResumeDO::getId, id)
                .setSql("match_count = match_count + 1"));
    }

}
