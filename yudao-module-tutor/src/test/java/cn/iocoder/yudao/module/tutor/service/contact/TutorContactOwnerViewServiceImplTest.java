package cn.iocoder.yudao.module.tutor.service.contact;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.tutor.controller.app.contact.vo.AppTutorContactRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class TutorContactOwnerViewServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorContactServiceImpl contactService;

    @Mock
    private TutorDemandMapper demandMapper;
    @Mock
    private TutorTeacherResumeMapper resumeMapper;

    @Test
    void getOwnerContact_whenDemandBelongsToViewer_returnsFullContactWithoutPointCost() {
        when(demandMapper.selectById(11L)).thenReturn(TutorDemandDO.builder()
                .id(11L).userId(100L).contactMobileEncrypt("13800000000").contactWechatEncrypt("wechat").build());

        AppTutorContactRespVO respVO = contactService.getOwnerContact(100L, "demand", 11L);

        assertEquals("13800000000", respVO.getMobile());
        assertEquals("wechat", respVO.getWechat());
        assertEquals(0, respVO.getPointCost());
    }

    @Test
    void getOwnerContact_whenDemandBelongsToAnotherUser_returnsNull() {
        when(demandMapper.selectById(11L)).thenReturn(TutorDemandDO.builder().id(11L).userId(200L).build());

        assertNull(contactService.getOwnerContact(100L, "demand", 11L));
    }
}
