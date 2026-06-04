package cn.iocoder.yudao.module.tutor.dal.mysql.certification;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.controller.admin.certification.vo.AdminTutorCertificationPageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.certification.TutorCertificationDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TutorCertificationMapper extends BaseMapperX<TutorCertificationDO> {

    default TutorCertificationDO selectByUserId(Long userId) {
        return selectOne(TutorCertificationDO::getUserId, userId);
    }

    default PageResult<TutorCertificationDO> selectPage(AdminTutorCertificationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TutorCertificationDO>()
                .eqIfPresent(TutorCertificationDO::getStatus, reqVO.getStatus())
                .likeIfPresent(TutorCertificationDO::getRealName, reqVO.getRealName())
                .eqIfPresent(TutorCertificationDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(TutorCertificationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TutorCertificationDO::getStatus)
                .orderByDesc(TutorCertificationDO::getId));
    }

}
