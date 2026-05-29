package cn.iocoder.yudao.module.tutor.service.resume;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import cn.iocoder.yudao.module.tutor.service.city.TutorCityService;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import cn.iocoder.yudao.module.tutor.service.teacher.TutorTeacherProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.PUBLISH_STATUS_NOT_VISIBLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TutorTeacherResumeServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorTeacherResumeServiceImpl resumeService;

    @Mock
    private TutorTeacherResumeMapper resumeMapper;
    @Mock
    private TutorTeacherProfileService teacherProfileService;
    @Mock
    private TutorCityService cityService;
    @Mock
    private TutorNotifyService tutorNotifyService;

    @Test
    void viewResumeForDetail_whenOwnerAndWaitingAudit_returnsResume() {
        TutorTeacherResumeDO resume = TutorTeacherResumeDO.builder()
                .id(11L)
                .userId(100L)
                .status(TutorPublishStatusEnum.WAIT_AUDIT.getStatus())
                .auditStatus(TutorAuditStatusEnum.WAITING.getStatus())
                .build();
        when(resumeMapper.selectById(11L)).thenReturn(resume);

        TutorTeacherResumeDO result = resumeService.viewResumeForDetail(100L, 11L);

        assertEquals(resume, result);
        verify(resumeMapper).updateViewCountIncr(11L);
    }

    @Test
    void viewResumeForDetail_whenNotOwnerAndWaitingAudit_throwsNotVisible() {
        TutorTeacherResumeDO resume = TutorTeacherResumeDO.builder()
                .id(11L)
                .userId(100L)
                .status(TutorPublishStatusEnum.WAIT_AUDIT.getStatus())
                .auditStatus(TutorAuditStatusEnum.WAITING.getStatus())
                .build();
        when(resumeMapper.selectById(11L)).thenReturn(resume);

        assertServiceException(() -> resumeService.viewResumeForDetail(200L, 11L),
                PUBLISH_STATUS_NOT_VISIBLE);
    }

}
