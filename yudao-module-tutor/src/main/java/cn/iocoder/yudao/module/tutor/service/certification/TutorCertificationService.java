package cn.iocoder.yudao.module.tutor.service.certification;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.certification.vo.AdminTutorCertificationAuditReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.certification.vo.AdminTutorCertificationPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.certification.vo.AppTutorCertificationSubmitReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.certification.TutorCertificationDO;

public interface TutorCertificationService {

    TutorCertificationDO getCertification(Long userId);

    TutorCertificationDO submitCertification(Long userId, AppTutorCertificationSubmitReqVO reqVO);

    PageResult<TutorCertificationDO> getCertificationPage(AdminTutorCertificationPageReqVO reqVO);

    TutorCertificationDO auditCertification(Long auditorId, AdminTutorCertificationAuditReqVO reqVO);

}
