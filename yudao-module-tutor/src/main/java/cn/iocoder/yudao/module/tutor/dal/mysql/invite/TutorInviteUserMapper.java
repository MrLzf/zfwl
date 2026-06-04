package cn.iocoder.yudao.module.tutor.dal.mysql.invite;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.invite.TutorInviteUserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TutorInviteUserMapper extends BaseMapperX<TutorInviteUserDO> {

    default TutorInviteUserDO selectByUserId(Long userId) {
        return selectOne(TutorInviteUserDO::getUserId, userId);
    }

    default TutorInviteUserDO selectByInviteCode(String inviteCode) {
        return selectOne(TutorInviteUserDO::getInviteCode, inviteCode);
    }
}
