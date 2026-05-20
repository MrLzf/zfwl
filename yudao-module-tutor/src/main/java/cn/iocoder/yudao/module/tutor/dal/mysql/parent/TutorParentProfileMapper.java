package cn.iocoder.yudao.module.tutor.dal.mysql.parent;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.parent.TutorParentProfileDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TutorParentProfileMapper extends BaseMapperX<TutorParentProfileDO> {

    default TutorParentProfileDO selectByUserId(Long userId) {
        return selectOne(TutorParentProfileDO::getUserId, userId);
    }

}
