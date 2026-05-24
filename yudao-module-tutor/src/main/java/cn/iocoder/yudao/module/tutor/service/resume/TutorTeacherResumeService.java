package cn.iocoder.yudao.module.tutor.service.resume;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.publish.vo.AdminTutorPublishAuditReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.resume.vo.AdminTutorTeacherResumePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.resume.vo.AppTutorTeacherResumeSaveReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.square.vo.AppTutorTeacherResumePageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;

import java.util.List;

public interface TutorTeacherResumeService {

    TutorTeacherResumeDO createResume(Long userId, AppTutorTeacherResumeSaveReqVO reqVO);

    TutorTeacherResumeDO updateResume(Long userId, Long id, AppTutorTeacherResumeSaveReqVO reqVO);

    void offlineResume(Long userId, Long id);

    TutorTeacherResumeDO offlineResumeByAdmin(Long id);

    List<TutorTeacherResumeDO> getMyResumeList(Long userId);

    PageResult<TutorTeacherResumeDO> getResumePage(AdminTutorTeacherResumePageReqVO reqVO);

    TutorTeacherResumeDO auditResume(Long auditorId, AdminTutorPublishAuditReqVO reqVO);

    PageResult<TutorTeacherResumeDO> getSquareResumePage(AppTutorTeacherResumePageReqVO reqVO);

    TutorTeacherResumeDO getSquareResume(Long id);

    TutorTeacherResumeDO viewSquareResume(Long id);

}
