package cn.iocoder.yudao.module.tutor.service.complaint;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.complaint.vo.AdminTutorComplaintHandleReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.complaint.vo.AdminTutorComplaintPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.complaint.vo.AppTutorComplaintCreateReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.complaint.TutorComplaintDO;

import java.util.List;

public interface TutorComplaintService {

    TutorComplaintDO createComplaint(Long userId, AppTutorComplaintCreateReqVO reqVO);

    List<TutorComplaintDO> getMyComplaintList(Long userId);

    PageResult<TutorComplaintDO> getComplaintPage(AdminTutorComplaintPageReqVO reqVO);

    TutorComplaintDO handleComplaint(Long handlerId, AdminTutorComplaintHandleReqVO reqVO);
}
