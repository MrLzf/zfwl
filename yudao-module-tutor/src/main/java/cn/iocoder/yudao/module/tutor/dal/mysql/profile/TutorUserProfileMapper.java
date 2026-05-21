package cn.iocoder.yudao.module.tutor.dal.mysql.profile;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.controller.admin.profile.vo.AdminTutorUserProfilePageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface TutorUserProfileMapper extends BaseMapperX<TutorUserProfileDO> {

    default TutorUserProfileDO selectByUserId(Long userId) {
        return selectOne(TutorUserProfileDO::getUserId, userId);
    }

    default PageResult<TutorUserProfileDO> selectPage(AdminTutorUserProfilePageReqVO reqVO, Collection<Long> userIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TutorUserProfileDO>()
                .eqIfPresent(TutorUserProfileDO::getUserId, reqVO.getUserId())
                .inIfPresent(TutorUserProfileDO::getUserId, userIds)
                .eqIfPresent(TutorUserProfileDO::getRole, reqVO.getRole())
                .eqIfPresent(TutorUserProfileDO::getCityCode, reqVO.getCityCode())
                .betweenIfPresent(TutorUserProfileDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TutorUserProfileDO::getId));
    }

}
