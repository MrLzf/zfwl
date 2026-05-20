package cn.iocoder.yudao.module.tutor.service.demand;

import cn.iocoder.yudao.module.tutor.controller.app.demand.vo.AppTutorDemandSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;

import java.util.List;

public interface TutorDemandService {

    TutorDemandDO createDemand(Long userId, AppTutorDemandSaveReqVO reqVO);

    TutorDemandDO updateDemand(Long userId, Long id, AppTutorDemandSaveReqVO reqVO);

    void offlineDemand(Long userId, Long id);

    List<TutorDemandDO> getMyDemandList(Long userId);

}
