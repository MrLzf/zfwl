package cn.iocoder.yudao.module.tutor.service.history;

import cn.iocoder.yudao.module.tutor.dal.dataobject.history.TutorBrowseHistoryDO;

import java.util.List;

public interface TutorBrowseHistoryService {

    void recordBrowseHistory(Long userId, String targetType, Long targetId, Long targetUserId,
                             String title, String cityCode, String cityName);

    List<TutorBrowseHistoryDO> getMyBrowseHistoryList(Long userId);

    void deleteMyBrowseHistory(Long userId, Long id);

    void clearMyBrowseHistory(Long userId);

}
