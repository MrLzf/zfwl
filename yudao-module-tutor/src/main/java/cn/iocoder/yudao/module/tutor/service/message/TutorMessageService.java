package cn.iocoder.yudao.module.tutor.service.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessagePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessageRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessageSummaryRespVO;

public interface TutorMessageService {

    AppTutorMessageSummaryRespVO getSummary(Long userId);

    PageResult<AppTutorMessageRespVO> getPage(Long userId, AppTutorMessagePageReqVO pageReqVO);

    void read(Long userId, Long id);

    void readAll(Long userId, String category);

}
