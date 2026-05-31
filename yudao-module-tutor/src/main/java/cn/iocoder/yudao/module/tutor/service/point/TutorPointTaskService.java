package cn.iocoder.yudao.module.tutor.service.point;

import cn.iocoder.yudao.module.tutor.controller.app.point.vo.AppTutorPointTaskRespVO;

import java.util.List;

public interface TutorPointTaskService {

    List<AppTutorPointTaskRespVO> getTaskList(Long userId);

}
