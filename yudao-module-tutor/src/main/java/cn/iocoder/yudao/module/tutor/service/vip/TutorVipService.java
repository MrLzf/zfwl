package cn.iocoder.yudao.module.tutor.service.vip;

import cn.iocoder.yudao.module.tutor.dal.dataobject.vip.TutorVipRecordDO;

public interface TutorVipService {

    TutorVipRecordDO buyVip(Long userId, Long configId);

    TutorVipRecordDO getActiveVip(Long userId);

    boolean isVip(Long userId);

    int calculateContactPointCost(Long userId, int originalCost);

    void grantMonthlyGiftPoints();
}
