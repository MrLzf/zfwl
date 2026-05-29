package cn.iocoder.yudao.module.tutor.service.demand;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.demand.vo.AdminTutorDemandPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.publish.vo.AdminTutorPublishAuditReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.demand.vo.AppTutorDemandSaveReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.square.vo.AppTutorDemandPageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;

import java.util.List;

public interface TutorDemandService {

    TutorDemandDO createDemand(Long userId, AppTutorDemandSaveReqVO reqVO);

    TutorDemandDO updateDemand(Long userId, Long id, AppTutorDemandSaveReqVO reqVO);

    void offlineDemand(Long userId, Long id);

    TutorDemandDO offlineDemandByAdmin(Long id);

    List<TutorDemandDO> getMyDemandList(Long userId);

    PageResult<TutorDemandDO> getDemandPage(AdminTutorDemandPageReqVO reqVO);

    TutorDemandDO auditDemand(Long auditorId, AdminTutorPublishAuditReqVO reqVO);

    PageResult<TutorDemandDO> getSquareDemandPage(AppTutorDemandPageReqVO reqVO);

    TutorDemandDO getSquareDemand(Long id);

    TutorDemandDO viewSquareDemand(Long id);

    TutorDemandDO viewDemandForDetail(Long viewerUserId, Long id);

}
