package cn.iocoder.yudao.module.tutor.service.certification;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.tutor.controller.app.certification.vo.AppTutorCertificationSubmitReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.certification.TutorCertificationDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.certification.TutorCertificationMapper;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import cn.iocoder.yudao.module.tutor.service.teacher.TutorTeacherProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TutorCertificationServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorCertificationServiceImpl certificationService;

    @Mock
    private TutorCertificationMapper certificationMapper;
    @Mock
    private TutorTeacherProfileService teacherProfileService;
    @Mock
    private TutorNotifyService tutorNotifyService;

    @Test
    void submitCertification_whenTeacherProfileMissing_recoversDraftProfile() {
        Long userId = 100L;
        when(teacherProfileService.getOrCreateTeacherProfile(userId))
                .thenReturn(TutorTeacherProfileDO.builder().id(10L).userId(userId).build());

        certificationService.submitCertification(userId, buildSubmitReqVO());

        verify(teacherProfileService).getOrCreateTeacherProfile(userId);
        verify(certificationMapper).insert(any(TutorCertificationDO.class));
    }

    private AppTutorCertificationSubmitReqVO buildSubmitReqVO() {
        AppTutorCertificationSubmitReqVO reqVO = new AppTutorCertificationSubmitReqVO();
        reqVO.setRealName("张三");
        reqVO.setIdCardNo("110101199001011234");
        reqVO.setEducationFileUrl("https://example.com/education.jpg");
        reqVO.setTeacherCertFileUrl("https://example.com/certificate.jpg");
        return reqVO;
    }

}
