package cn.iocoder.yudao.module.tutor.dal.mysql.recharge;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.dal.dataobject.recharge.TutorPointPackageDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TutorPointPackageMapper extends BaseMapperX<TutorPointPackageDO> {

    default List<TutorPointPackageDO> selectEnabledList() {
        return selectList(new LambdaQueryWrapperX<TutorPointPackageDO>()
                .eq(TutorPointPackageDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByAsc(TutorPointPackageDO::getSort)
                .orderByAsc(TutorPointPackageDO::getId));
    }
}
