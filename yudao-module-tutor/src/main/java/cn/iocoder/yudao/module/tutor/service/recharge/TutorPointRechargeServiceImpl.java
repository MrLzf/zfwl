package cn.iocoder.yudao.module.tutor.service.recharge;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import cn.iocoder.yudao.module.pay.api.order.PayOrderApi;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderCreateReqDTO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.recharge.TutorPointPackageDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.recharge.TutorPointRechargeOrderDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.recharge.TutorPointPackageMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.recharge.TutorPointRechargeOrderMapper;
import cn.iocoder.yudao.module.tutor.enums.recharge.TutorRechargeOrderStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.POINT_PACKAGE_NOT_EXISTS;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.POINT_RECHARGE_ORDER_NOT_EXISTS;

@Service
@Validated
public class TutorPointRechargeServiceImpl implements TutorPointRechargeService {

    private static final String PAY_APP_KEY = "tutor";
    private static final String ORDER_PREFIX = "TUTOR_RECHARGE_";

    @Resource
    private TutorPointPackageMapper pointPackageMapper;
    @Resource
    private TutorPointRechargeOrderMapper rechargeOrderMapper;
    @Resource
    private PayOrderApi payOrderApi;
    @Resource
    private MemberPointApi memberPointApi;

    @Override
    public List<TutorPointPackageDO> getEnabledPackageList() {
        return pointPackageMapper.selectEnabledList();
    }

    @Override
    @Transactional
    public TutorPointRechargeOrderDO createOrder(Long userId, Long packageId, String userIp) {
        TutorPointPackageDO pointPackage = pointPackageMapper.selectById(packageId);
        if (pointPackage == null || !CommonStatusEnum.ENABLE.getStatus().equals(pointPackage.getStatus())) {
            throw exception(POINT_PACKAGE_NOT_EXISTS);
        }
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(15);
        TutorPointRechargeOrderDO order = TutorPointRechargeOrderDO.builder()
                .userId(userId)
                .packageId(packageId)
                .packageName(pointPackage.getName())
                .point(pointPackage.getPoint())
                .bonusPoint(pointPackage.getBonusPoint() == null ? 0 : pointPackage.getBonusPoint())
                .totalPoint(pointPackage.getPoint() + (pointPackage.getBonusPoint() == null ? 0 : pointPackage.getBonusPoint()))
                .price(pointPackage.getPrice())
                .status(TutorRechargeOrderStatusEnum.WAIT_PAY.getStatus())
                .expireTime(expireTime)
                .build();
        rechargeOrderMapper.insert(order);
        String merchantOrderId = ORDER_PREFIX + order.getId();
        Long payOrderId = payOrderApi.createOrder(buildPayOrder(userId, userIp, order, merchantOrderId, expireTime));
        rechargeOrderMapper.updateById(TutorPointRechargeOrderDO.builder()
                .id(order.getId())
                .merchantOrderId(merchantOrderId)
                .payOrderId(payOrderId)
                .build());
        order.setMerchantOrderId(merchantOrderId);
        order.setPayOrderId(payOrderId);
        return order;
    }

    @Override
    @Transactional
    public void notifyPaid(PayOrderNotifyReqDTO notifyReqDTO) {
        TutorPointRechargeOrderDO order = rechargeOrderMapper.selectByMerchantOrderId(notifyReqDTO.getMerchantOrderId());
        if (order == null || !Objects.equals(order.getPayOrderId(), notifyReqDTO.getPayOrderId())) {
            throw exception(POINT_RECHARGE_ORDER_NOT_EXISTS);
        }
        if (TutorRechargeOrderStatusEnum.PAID.getStatus().equals(order.getStatus())) {
            return;
        }
        memberPointApi.addPoint(order.getUserId(), order.getTotalPoint(),
                MemberPointBizTypeEnum.TUTOR_POINT_RECHARGE.getType(), order.getMerchantOrderId());
        rechargeOrderMapper.updateById(TutorPointRechargeOrderDO.builder()
                .id(order.getId())
                .status(TutorRechargeOrderStatusEnum.PAID.getStatus())
                .payTime(LocalDateTime.now())
                .build());
    }

    private PayOrderCreateReqDTO buildPayOrder(Long userId, String userIp, TutorPointRechargeOrderDO order,
                                               String merchantOrderId, LocalDateTime expireTime) {
        PayOrderCreateReqDTO reqDTO = new PayOrderCreateReqDTO();
        reqDTO.setAppKey(PAY_APP_KEY);
        reqDTO.setUserIp(userIp);
        reqDTO.setUserId(userId);
        reqDTO.setUserType(UserTypeEnum.MEMBER.getValue());
        reqDTO.setMerchantOrderId(merchantOrderId);
        reqDTO.setSubject(order.getPackageName());
        reqDTO.setBody("家教积分充值");
        reqDTO.setPrice(order.getPrice());
        reqDTO.setExpireTime(expireTime);
        return reqDTO;
    }
}
