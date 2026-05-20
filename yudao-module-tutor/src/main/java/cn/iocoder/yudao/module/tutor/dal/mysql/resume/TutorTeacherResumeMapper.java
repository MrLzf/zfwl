package cn.iocoder.yudao.module.tutor.dal.mysql.resume;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
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

}
