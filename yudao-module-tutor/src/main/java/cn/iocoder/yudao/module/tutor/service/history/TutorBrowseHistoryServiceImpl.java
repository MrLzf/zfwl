package cn.iocoder.yudao.module.tutor.service.history;

import cn.iocoder.yudao.module.tutor.dal.dataobject.history.TutorBrowseHistoryDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.history.TutorBrowseHistoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

@Service
@Validated
public class TutorBrowseHistoryServiceImpl implements TutorBrowseHistoryService {

    @Resource
    private TutorBrowseHistoryMapper browseHistoryMapper;

    @Override
    public void recordBrowseHistory(Long userId, String targetType, Long targetId, Long targetUserId,
                                    String title, String cityCode, String cityName) {
        if (userId == null) {
            return;
        }
        browseHistoryMapper.deleteByUserIdAndTarget(userId, targetType, targetId);
        browseHistoryMapper.insert(TutorBrowseHistoryDO.builder()
                .userId(userId)
                .targetType(targetType)
                .targetId(targetId)
                .targetUserId(targetUserId)
                .title(title)
                .cityCode(cityCode)
                .cityName(cityName)
                .build());
    }

    @Override
    public List<TutorBrowseHistoryDO> getMyBrowseHistoryList(Long userId) {
        return browseHistoryMapper.selectListByUserId(userId);
    }

    @Override
    public void deleteMyBrowseHistory(Long userId, Long id) {
        browseHistoryMapper.deleteByIdAndUserId(id, userId);
    }

    @Override
    public void clearMyBrowseHistory(Long userId) {
        browseHistoryMapper.deleteByUserId(userId);
    }

}
