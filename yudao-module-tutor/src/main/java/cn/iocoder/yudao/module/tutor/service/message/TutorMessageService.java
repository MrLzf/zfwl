package cn.iocoder.yudao.module.tutor.service.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessagePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessageRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.AppTutorMessageSummaryRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.chat.AppTutorChatMessagePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.message.vo.chat.AppTutorChatMessageSendReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.message.TutorChatMessageDO;

public interface TutorMessageService {

    AppTutorMessageSummaryRespVO getSummary(Long userId);

    PageResult<AppTutorMessageRespVO> getPage(Long userId, AppTutorMessagePageReqVO pageReqVO);

    void read(Long userId, Long id);

    void readAll(Long userId, String category);

    TutorChatMessageDO sendChatMessage(Long userId, AppTutorChatMessageSendReqVO reqVO);

    PageResult<TutorChatMessageDO> getChatMessagePage(Long userId, AppTutorChatMessagePageReqVO reqVO);

}
