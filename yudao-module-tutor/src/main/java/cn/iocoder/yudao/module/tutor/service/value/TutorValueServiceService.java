package cn.iocoder.yudao.module.tutor.service.value;

import cn.iocoder.yudao.module.tutor.controller.app.value.vo.AppTutorValueServiceBuyReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.value.TutorValueServiceConfigDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.value.TutorValueServiceOrderDO;

import java.util.List;

public interface TutorValueServiceService {

    List<TutorValueServiceConfigDO> getEnabledConfigList(String targetType);

    TutorValueServiceOrderDO buyService(Long userId, AppTutorValueServiceBuyReqVO reqVO);
}
