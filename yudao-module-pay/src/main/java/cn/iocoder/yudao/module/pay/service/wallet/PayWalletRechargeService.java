package cn.iocoder.yudao.module.pay.service.wallet;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.recharge.AppPayWalletRechargeCreateReqVO;
import cn.iocoder.yudao.module.pay.controller.admin.wallet.vo.recharge.WalletRechargePageReqVO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;

/**
 * 钱包充值 Service 接口
 *
 * @author jason
 */
public interface PayWalletRechargeService {

    /**
     * 创建钱包充值记录（发起充值）
     *
     * @param userId      用户编号
     * @param userType    用户类型
     * @param createReqVO 钱包充值请求 VO
     * @param userIp  用户Ip
     * @return 钱包充值记录
     */
    PayWalletRechargeDO createWalletRecharge(Long userId, Integer userType, String userIp,
                                             AppPayWalletRechargeCreateReqVO createReqVO);

    /**
     * 获得钱包充值记录分页
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @param pageReqVO 分页请求
     * @param payStatus 是否支付
     * @return 钱包充值记录分页
     */
    PageResult<PayWalletRechargeDO> getWalletRechargePackagePage(Long userId, Integer userType,
                                                                 PageParam pageReqVO, Boolean payStatus);

    /**
     * 更新钱包充值成功
     *
     * @param id         钱包充值记录编号
     * @param payOrderId 支付订单编号
     */
    void updateWalletRechargerPaid(Long id, Long payOrderId);

    /**
     * 发起钱包充值退款
     *
     * @param id     钱包充值编号
     * @param userIp 用户 ip 地址
     */
    void refundWalletRecharge(Long id, String userIp);

    /**
     * 更新钱包充值记录为已退款
     *
     * @param id          钱包充值记录编号
     * @param refundId    钱包充值退款编号（格式：{id}-refund）
     * @param payRefundId 退款单id
     */
    void updateWalletRechargeRefunded(Long id, String refundId, Long payRefundId);


    /**
     * \u83b7\u5f97\u94b1\u5305\u5145\u503c\u8bb0\u5f55\u5206\u9875\uff08\u7ba1\u7406\u540e\u53f0\uff09
     *
     * @param pageReqVO \u5206\u9875\u8bf7\u6c42
     * @return \u94b1\u5305\u5145\u503c\u8bb0\u5f55\u5206\u9875
     */
    PageResult<PayWalletRechargeDO> getRechargePage(WalletRechargePageReqVO pageReqVO);
}
