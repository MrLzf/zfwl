package cn.iocoder.yudao.module.tutor.service.subscribe;

import cn.iocoder.yudao.module.tutor.controller.app.subscribe.vo.AppTutorSubscribeSettingReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.subscribe.TutorSubscribeMessageRecordDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.subscribe.TutorSubscribeSettingDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.subscribe.TutorSubscribeMessageRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.subscribe.TutorSubscribeSettingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TutorSubscribeMessageServiceImpl implements TutorSubscribeMessageService {

    @Resource
    private TutorSubscribeSettingMapper settingMapper;
    @Resource
    private TutorSubscribeMessageRecordMapper recordMapper;

    @Override
    public List<TutorSubscribeSettingDO> getSettings(Long userId) {
        return settingMapper.selectListByUserId(userId);
    }

    @Override
    public void updateSetting(Long userId, AppTutorSubscribeSettingReqVO reqVO) {
        TutorSubscribeSettingDO setting = settingMapper.selectByUserIdAndType(userId, reqVO.getNoticeType());
        if (setting == null) {
            settingMapper.insert(TutorSubscribeSettingDO.builder()
                    .userId(userId).noticeType(reqVO.getNoticeType()).enabled(reqVO.getEnabled()).build());
            return;
        }
        settingMapper.updateById(TutorSubscribeSettingDO.builder().id(setting.getId()).enabled(reqVO.getEnabled()).build());
    }

    @Override
    public void trigger(Long userId, String noticeType, String title, String content, String bizId, String targetType, Long targetId) {
        TutorSubscribeSettingDO setting = settingMapper.selectByUserIdAndType(userId, noticeType);
        boolean enabled = setting == null || !Boolean.FALSE.equals(setting.getEnabled());
        recordMapper.insert(TutorSubscribeMessageRecordDO.builder().userId(userId).noticeType(noticeType)
                .title(title).content(content).bizId(bizId).targetType(targetType).targetId(targetId)
                .status(enabled ? 0 : 30).build());
    }
}
