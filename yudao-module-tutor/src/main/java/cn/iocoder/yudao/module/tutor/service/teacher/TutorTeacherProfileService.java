package cn.iocoder.yudao.module.tutor.service.teacher;

import cn.iocoder.yudao.module.tutor.controller.app.teacher.vo.AppTutorTeacherProfileSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;

public interface TutorTeacherProfileService {

    TutorTeacherProfileDO getTeacherProfile(Long userId);

    TutorTeacherProfileDO saveTeacherProfile(Long userId, AppTutorTeacherProfileSaveReqVO reqVO);

    void updateCertificationStatus(Long userId, Integer certificationStatus);

}
