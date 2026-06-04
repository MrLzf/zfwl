package cn.iocoder.yudao.module.tutor.service.subscribe;

import cn.iocoder.yudao.module.tutor.controller.app.subscribe.vo.AppTutorSubscribeSettingReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.subscribe.TutorSubscribeSettingDO;

import java.util.List;

public interface TutorSubscribeMessageService {

    List<TutorSubscribeSettingDO> getSettings(Long userId);

    void updateSetting(Long userId, AppTutorSubscribeSettingReqVO reqVO);

    void trigger(Long userId, String noticeType, String title, String content, String bizId, String targetType, Long targetId);
}
