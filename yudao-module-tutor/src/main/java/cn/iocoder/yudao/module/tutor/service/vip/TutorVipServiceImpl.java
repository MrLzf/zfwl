package cn.iocoder.yudao.module.tutor.service.vip;

import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.tutor.dal.dataobject.value.TutorValueServiceConfigDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.vip.TutorVipRecordDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.value.TutorValueServiceConfigMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.vip.TutorVipRecordMapper;
import cn.iocoder.yudao.module.tutor.enums.value.TutorValueServiceTypeEnum;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.VIP_CONFIG_NOT_EXISTS;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.VIP_POINT_NOT_ENOUGH;

@Service
public class TutorVipServiceImpl implements TutorVipService {

    private static final int DEFAULT_MONTHLY_GIFT_POINT = 30;

    @Resource
    private TutorValueServiceConfigMapper tutorValueServiceConfigMapper;
    @Resource
    private TutorVipRecordMapper vipRecordMapper;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private MemberPointApi memberPointApi;
    @Resource
    private TutorNotifyService tutorNotifyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TutorVipRecordDO buyVip(Long userId, Long configId) {
        TutorValueServiceConfigDO config = tutorValueServiceConfigMapper.selectById(configId);
        if (config == null || config.getStatus() == null || config.getStatus() != 0
                || !TutorValueServiceTypeEnum.VIP.getType().equals(config.getServiceType())) {
            throw exception(VIP_CONFIG_NOT_EXISTS);
        }
        MemberUserRespDTO user = memberUserApi.getUser(userId);
        if (user == null || user.getPoint() == null || user.getPoint() < config.getPointPrice()) {
            throw exception(VIP_POINT_NOT_ENOUGH);
        }
        LocalDateTime now = LocalDateTime.now();
        TutorVipRecordDO active = vipRecordMapper.selectActiveByUserId(userId, now);
        LocalDateTime start = active == null ? now : active.getEndTime();
        TutorVipRecordDO record = TutorVipRecordDO.builder()
                .userId(userId).configId(configId).pointPrice(config.getPointPrice())
                .startTime(start).endTime(start.plusHours(config.getDurationHours()))
                .monthlyGiftPoint(DEFAULT_MONTHLY_GIFT_POINT).status(0).build();
        vipRecordMapper.insert(record);
        memberPointApi.reducePoint(userId, config.getPointPrice(), MemberPointBizTypeEnum.TUTOR_VALUE_SERVICE.getType(),
                "vip:" + record.getId());
        tutorNotifyService.sendPointChanged(userId, "购买 VIP", -config.getPointPrice(),
                user.getPoint() - config.getPointPrice(), "point", "vip", "vip:" + record.getId(), "vip", record.getId());
        return record;
    }

    @Override
    public TutorVipRecordDO getActiveVip(Long userId) {
        return vipRecordMapper.selectActiveByUserId(userId, LocalDateTime.now());
    }

    @Override
    public boolean isVip(Long userId) {
        return getActiveVip(userId) != null;
    }

    @Override
    public int calculateContactPointCost(Long userId, int originalCost) {
        return isVip(userId) ? Math.max(1, originalCost * 8 / 10) : originalCost;
    }

    @Override
    public void grantMonthlyGiftPoints() {
        LocalDateTime now = LocalDateTime.now();
        List<TutorVipRecordDO> records = vipRecordMapper.selectActiveList(now);
        for (TutorVipRecordDO record : records) {
            if (record.getLastGiftTime() != null && record.getLastGiftTime().plusMonths(1).isAfter(now)) {
                continue;
            }
            Integer giftPoint = record.getMonthlyGiftPoint() == null ? DEFAULT_MONTHLY_GIFT_POINT : record.getMonthlyGiftPoint();
            memberPointApi.addPoint(record.getUserId(), giftPoint, MemberPointBizTypeEnum.TUTOR_VALUE_SERVICE.getType(),
                    "vip-monthly:" + record.getId() + ":" + now.toLocalDate());
            vipRecordMapper.updateById(TutorVipRecordDO.builder().id(record.getId()).lastGiftTime(now).build());
            tutorNotifyService.sendPointChanged(record.getUserId(), "VIP 每月赠送积分", giftPoint);
        }
    }
}
