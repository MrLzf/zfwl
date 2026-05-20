package cn.iocoder.yudao.module.tutor.dal.mysql.teacher;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TutorTeacherProfileMapper extends BaseMapperX<TutorTeacherProfileDO> {

    default TutorTeacherProfileDO selectByUserId(Long userId) {
        return selectOne(TutorTeacherProfileDO::getUserId, userId);
    }

}
