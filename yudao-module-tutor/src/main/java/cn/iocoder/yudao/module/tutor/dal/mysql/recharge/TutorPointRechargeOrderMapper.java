package cn.iocoder.yudao.module.tutor.dal.mysql.recharge;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.recharge.TutorPointRechargeOrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TutorPointRechargeOrderMapper extends BaseMapperX<TutorPointRechargeOrderDO> {

    default TutorPointRechargeOrderDO selectByMerchantOrderId(String merchantOrderId) {
        return selectOne(TutorPointRechargeOrderDO::getMerchantOrderId, merchantOrderId);
    }

    default PageResult<TutorPointRechargeOrderDO> selectPageByUserId(Long userId,
            cn.iocoder.yudao.framework.common.pojo.PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<TutorPointRechargeOrderDO>()
                .eq(TutorPointRechargeOrderDO::getUserId, userId)
                .orderByDesc(TutorPointRechargeOrderDO::getId));
    }
}
