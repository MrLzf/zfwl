package cn.iocoder.yudao.module.tutor.dal.mysql.resume;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.controller.admin.resume.vo.AdminTutorTeacherResumePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.square.vo.AppTutorTeacherResumePageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
                .last("ORDER BY EXISTS (SELECT 1 FROM tutor_vip_record v WHERE v.user_id = tutor_teacher_resume.user_id AND v.status = 0 AND v.start_time <= NOW() AND v.end_time > NOW() AND v.deleted = b'0') DESC, id DESC"));
    }

    default PageResult<TutorTeacherResumeDO> selectSquarePage(AppTutorTeacherResumePageReqVO reqVO) {
        QueryWrapper<TutorTeacherResumeDO> wrapper = new QueryWrapper<TutorTeacherResumeDO>()
                .eq("status", TutorPublishStatusEnum.SHOWING.getStatus())
                .eq("audit_status", TutorAuditStatusEnum.APPROVED.getStatus())
                .eq(reqVO.getCityCode() != null, "city_code", reqVO.getCityCode())
                .like(reqVO.getSubject() != null, "subjects", reqVO.getSubject())
                .like(reqVO.getTeachMode() != null, "teach_modes", reqVO.getTeachMode() == null ? null : reqVO.getTeachMode().toString())
                .ge(reqVO.getPriceMin() != null, "hourly_price", reqVO.getPriceMin())
                .le(reqVO.getPriceMax() != null, "hourly_price", reqVO.getPriceMax())
                .eq(reqVO.getFreeTrialEnabled() != null, "free_trial_enabled", reqVO.getFreeTrialEnabled());
        wrapper.exists("SELECT 1 FROM tutor_recommend_weight_config rw WHERE rw.scene = 'square_resume' "
                + "AND (rw.city_code IS NULL OR rw.city_code = tutor_teacher_resume.city_code) "
                + "AND rw.status = 0 AND rw.deleted = b'0'");
        if (reqVO.getCityCode() != null && reqVO.getLongitude() != null && reqVO.getLatitude() != null) {
            wrapper.apply("geohash IS NULL OR geohash LIKE CONCAT(LEFT(geohash, 4), '%')");
        }
        cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper.appendDistanceConditionAndOrder(
                wrapper, reqVO.getLongitude(), reqVO.getLatitude(), reqVO.getDistanceKm(), reqVO.getSortType());
        return selectPage(reqVO, wrapper);
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
