package cn.iocoder.yudao.module.tutor.dal.mysql.complaint;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.controller.admin.complaint.vo.AdminTutorComplaintPageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.complaint.TutorComplaintDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TutorComplaintMapper extends BaseMapperX<TutorComplaintDO> {

    default List<TutorComplaintDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<TutorComplaintDO>()
                .eq(TutorComplaintDO::getUserId, userId)
                .orderByDesc(TutorComplaintDO::getId));
    }

    default PageResult<TutorComplaintDO> selectPage(AdminTutorComplaintPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TutorComplaintDO>()
                .eqIfPresent(TutorComplaintDO::getUserId, reqVO.getUserId())
                .eqIfPresent(TutorComplaintDO::getTargetType, reqVO.getTargetType())
                .eqIfPresent(TutorComplaintDO::getTargetId, reqVO.getTargetId())
                .eqIfPresent(TutorComplaintDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(TutorComplaintDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TutorComplaintDO::getId));
    }
}
