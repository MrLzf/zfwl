package cn.iocoder.yudao.module.tutor.dal.mysql.invite;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.invite.TutorInviteRecordDO;
import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface TutorInviteRecordMapper extends BaseMapperX<TutorInviteRecordDO> {

    default Long selectCountByDeviceOrIp(String deviceId, String ip) {
        LambdaQueryWrapperX<TutorInviteRecordDO> wrapper = new LambdaQueryWrapperX<>();
        if (StrUtil.isNotBlank(deviceId) && StrUtil.isNotBlank(ip)) {
            wrapper.eq(TutorInviteRecordDO::getDeviceId, deviceId).or().eq(TutorInviteRecordDO::getIp, ip);
        } else if (StrUtil.isNotBlank(deviceId)) {
            wrapper.eq(TutorInviteRecordDO::getDeviceId, deviceId);
        } else if (StrUtil.isNotBlank(ip)) {
            wrapper.eq(TutorInviteRecordDO::getIp, ip);
        } else {
            return 0L;
        }
        return selectCount(wrapper);
    }

    default Long selectMonthlyCount(Long inviterUserId, LocalDateTime beginTime, LocalDateTime endTime) {
        return selectCount(new LambdaQueryWrapperX<TutorInviteRecordDO>()
                .eq(TutorInviteRecordDO::getInviterUserId, inviterUserId)
                .between(TutorInviteRecordDO::getCreateTime, beginTime, endTime));
    }
}
