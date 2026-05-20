package cn.iocoder.yudao.module.tutor.service.resume;

import cn.iocoder.yudao.module.tutor.controller.app.resume.vo.AppTutorTeacherResumeSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;

import java.util.List;

public interface TutorTeacherResumeService {

    TutorTeacherResumeDO createResume(Long userId, AppTutorTeacherResumeSaveReqVO reqVO);

    TutorTeacherResumeDO updateResume(Long userId, Long id, AppTutorTeacherResumeSaveReqVO reqVO);

    void offlineResume(Long userId, Long id);

    List<TutorTeacherResumeDO> getMyResumeList(Long userId);

}
