package cn.iocoder.yudao.module.tutor.service.match;

import cn.iocoder.yudao.module.tutor.dal.dataobject.match.TutorMatchRecordDO;

import java.util.List;

public interface TutorMatchService {
    TutorMatchRecordDO confirmMatch(Long userId, Long id);
    List<TutorMatchRecordDO> getMyMatchList(Long userId);
}
