package cn.iocoder.yudao.module.tutor.service.contact;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.contact.vo.AdminTutorContactPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.common.vo.AppTutorTargetReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.contact.vo.AppTutorContactRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.contact.TutorContactViewRecordDO;

import java.util.List;

public interface TutorContactService {
    AppTutorContactRespVO viewContact(Long viewerUserId, AppTutorTargetReqVO reqVO);
    List<TutorContactViewRecordDO> getMyContactRecordList(Long viewerUserId);
    PageResult<TutorContactViewRecordDO> getContactPage(AdminTutorContactPageReqVO reqVO);
}
