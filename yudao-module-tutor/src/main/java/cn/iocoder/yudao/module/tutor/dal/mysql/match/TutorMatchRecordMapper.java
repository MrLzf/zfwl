package cn.iocoder.yudao.module.tutor.dal.mysql.match;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.controller.admin.match.vo.AdminTutorMatchPageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.match.TutorMatchRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TutorMatchRecordMapper extends BaseMapperX<TutorMatchRecordDO> {

    default TutorMatchRecordDO selectByDemandAndTeacher(Long demandId, Long teacherUserId) {
        return selectOne(new LambdaQueryWrapperX<TutorMatchRecordDO>()
                .eq(TutorMatchRecordDO::getDemandId, demandId)
                .eq(TutorMatchRecordDO::getTeacherUserId, teacherUserId)
                .orderByDesc(TutorMatchRecordDO::getId)
                .last("LIMIT 1"));
    }

    default TutorMatchRecordDO selectByResumeAndParent(Long resumeId, Long parentUserId) {
        return selectOne(new LambdaQueryWrapperX<TutorMatchRecordDO>()
                .eq(TutorMatchRecordDO::getResumeId, resumeId)
                .eq(TutorMatchRecordDO::getParentUserId, parentUserId)
                .orderByDesc(TutorMatchRecordDO::getId)
                .last("LIMIT 1"));
    }

    default List<TutorMatchRecordDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<TutorMatchRecordDO>()
                .and(wrapper -> wrapper.eq(TutorMatchRecordDO::getParentUserId, userId)
                        .or().eq(TutorMatchRecordDO::getTeacherUserId, userId))
                .orderByDesc(TutorMatchRecordDO::getId));
    }

    default PageResult<TutorMatchRecordDO> selectPage(AdminTutorMatchPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TutorMatchRecordDO>()
                .eqIfPresent(TutorMatchRecordDO::getDemandId, reqVO.getDemandId())
                .eqIfPresent(TutorMatchRecordDO::getResumeId, reqVO.getResumeId())
                .eqIfPresent(TutorMatchRecordDO::getParentUserId, reqVO.getParentUserId())
                .eqIfPresent(TutorMatchRecordDO::getTeacherUserId, reqVO.getTeacherUserId())
                .eqIfPresent(TutorMatchRecordDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(TutorMatchRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TutorMatchRecordDO::getId));
    }

}
