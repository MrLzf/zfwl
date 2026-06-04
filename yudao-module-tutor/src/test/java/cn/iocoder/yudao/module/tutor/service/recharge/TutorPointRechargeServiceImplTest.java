package cn.iocoder.yudao.module.tutor.service.recharge;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import cn.iocoder.yudao.module.pay.api.order.PayOrderApi;
import cn.iocoder.yudao.module.tutor.dal.dataobject.recharge.TutorPointPackageDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.recharge.TutorPointRechargeOrderDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.recharge.TutorPointPackageMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.recharge.TutorPointRechargeOrderMapper;
import cn.iocoder.yudao.module.tutor.enums.recharge.TutorRechargeOrderStatusEnum;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TutorPointRechargeServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorPointRechargeServiceImpl rechargeService;

    @Mock
    private TutorPointPackageMapper pointPackageMapper;
    @Mock
    private TutorPointRechargeOrderMapper rechargeOrderMapper;
    @Mock
    private PayOrderApi payOrderApi;
    @Mock
    private MemberPointApi memberPointApi;

    @Test
    void createOrder_createsUnpaidRechargeOrderAndPayOrderWith15MinuteExpire() {
        TutorPointPackageDO pack = TutorPointPackageDO.builder().id(1L).name("100积分包")
                .point(100).bonusPoint(10).price(990).status(0).build();
        when(pointPackageMapper.selectById(1L)).thenReturn(pack);
        when(payOrderApi.createOrder(any())).thenReturn(88L);

        TutorPointRechargeOrderDO order = rechargeService.createOrder(100L, 1L, "127.0.0.1");

        assertEquals(110, order.getTotalPoint());
        assertEquals(TutorRechargeOrderStatusEnum.WAIT_PAY.getStatus(), order.getStatus());
        verify(rechargeOrderMapper).insert(any(TutorPointRechargeOrderDO.class));
        verify(payOrderApi).createOrder(argThat(req -> req.getExpireTime() != null
                && req.getExpireTime().isBefore(java.time.LocalDateTime.now().plusMinutes(16))));
        verify(rechargeOrderMapper).updateById(argThat((TutorPointRechargeOrderDO update) -> update.getPayOrderId().equals(88L)));
    }

    @Test
    void notifyPaid_addsPointOnceAndMarksOrderPaid() {
        TutorPointRechargeOrderDO order = TutorPointRechargeOrderDO.builder().id(10L).userId(100L)
                .merchantOrderId("TUTOR_RECHARGE_10").payOrderId(88L).totalPoint(110)
                .status(TutorRechargeOrderStatusEnum.WAIT_PAY.getStatus()).build();
        when(rechargeOrderMapper.selectByMerchantOrderId("TUTOR_RECHARGE_10")).thenReturn(order);

        rechargeService.notifyPaid(PayOrderNotifyReqDTO.builder()
                .merchantOrderId("TUTOR_RECHARGE_10").payOrderId(88L).build());

        verify(memberPointApi).addPoint(eq(100L), eq(110), anyInt(), eq("TUTOR_RECHARGE_10"));
        verify(rechargeOrderMapper).updateById(argThat((TutorPointRechargeOrderDO update) ->
                TutorRechargeOrderStatusEnum.PAID.getStatus().equals(update.getStatus())));
    }
}
