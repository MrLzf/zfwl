package cn.iocoder.yudao.module.tutor.service.demand;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.tutor.controller.app.demand.vo.AppTutorDemandSaveReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.parent.vo.AppTutorParentProfileSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.parent.TutorParentProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorTeachModeEnum;
import cn.iocoder.yudao.module.tutor.service.city.TutorCityService;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import cn.iocoder.yudao.module.tutor.service.parent.TutorParentProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TutorDemandServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorDemandServiceImpl demandService;

    @Mock
    private TutorDemandMapper demandMapper;
    @Mock
    private TutorParentProfileService parentProfileService;
    @Mock
    private TutorCityService cityService;
    @Mock
    private TutorNotifyService tutorNotifyService;

    @Test
    void createDemand_whenParentProfileMissing_autoSavesParentProfile() {
        Long userId = 100L;
        AppTutorDemandSaveReqVO reqVO = buildDemandReqVO();
        when(parentProfileService.saveParentProfile(eq(userId), any(AppTutorParentProfileSaveReqVO.class)))
                .thenReturn(TutorParentProfileDO.builder().id(10L).userId(userId).build());
        when(demandMapper.selectCountByUserIdAndStatuses(eq(userId), anyList())).thenReturn(0L);
        when(cityService.validateCityOpened("110100")).thenReturn(TutorCityDO.builder()
                .id(1L).code("110100").name("北京").build());

        demandService.createDemand(userId, reqVO);

        ArgumentCaptor<AppTutorParentProfileSaveReqVO> captor =
                ArgumentCaptor.forClass(AppTutorParentProfileSaveReqVO.class);
        verify(parentProfileService).saveParentProfile(eq(userId), captor.capture());
        AppTutorParentProfileSaveReqVO parentReqVO = captor.getValue();
        assertEquals(reqVO.getGrade(), parentReqVO.getChildGrade());
        assertEquals(reqVO.getSubjects(), parentReqVO.getSubjects());
        assertEquals(reqVO.getBudgetMin(), parentReqVO.getBudgetMin());
        assertEquals(reqVO.getBudgetMax(), parentReqVO.getBudgetMax());
        assertEquals(reqVO.getTeachMode(), parentReqVO.getTeachMode());
        assertEquals(reqVO.getDescription(), parentReqVO.getRemark());
        verify(demandMapper).insert(any(TutorDemandDO.class));
    }

    private AppTutorDemandSaveReqVO buildDemandReqVO() {
        AppTutorDemandSaveReqVO reqVO = new AppTutorDemandSaveReqVO();
        reqVO.setTitle("高三物理冲刺辅导");
        reqVO.setGrade("高三");
        reqVO.setSubjects("物理,数学");
        reqVO.setTeachMode(TutorTeachModeEnum.BOTH.getMode());
        reqVO.setBudgetMin(120);
        reqVO.setBudgetMax(240);
        reqVO.setDescription("希望老师周末下午上课");
        reqVO.setCityCode("110100");
        reqVO.setDistanceVisible(true);
        reqVO.setContactMobile("13800000000");
        reqVO.setContactWechat("wechat");
        return reqVO;
    }

}
