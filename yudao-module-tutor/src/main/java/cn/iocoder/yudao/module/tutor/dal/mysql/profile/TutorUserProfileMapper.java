package cn.iocoder.yudao.module.tutor.dal.mysql.profile;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TutorUserProfileMapper extends BaseMapperX<TutorUserProfileDO> {

    default TutorUserProfileDO selectByUserId(Long userId) {
        return selectOne(TutorUserProfileDO::getUserId, userId);
    }

}
