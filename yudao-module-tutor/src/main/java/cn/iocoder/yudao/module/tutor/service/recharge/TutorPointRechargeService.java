package cn.iocoder.yudao.module.tutor.service.recharge;

import cn.iocoder.yudao.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.recharge.TutorPointPackageDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.recharge.TutorPointRechargeOrderDO;

import java.util.List;

public interface TutorPointRechargeService {

    List<TutorPointPackageDO> getEnabledPackageList();

    TutorPointRechargeOrderDO createOrder(Long userId, Long packageId, String userIp);

    void notifyPaid(PayOrderNotifyReqDTO notifyReqDTO);
}
