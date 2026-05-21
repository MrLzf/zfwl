package cn.iocoder.yudao.module.tutor.service.match;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.match.vo.AdminTutorMatchPageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.match.TutorMatchRecordDO;

import java.util.List;

public interface TutorMatchService {
    TutorMatchRecordDO confirmMatch(Long userId, Long id);
    List<TutorMatchRecordDO> getMyMatchList(Long userId);
    PageResult<TutorMatchRecordDO> getMatchPage(AdminTutorMatchPageReqVO reqVO);
}
