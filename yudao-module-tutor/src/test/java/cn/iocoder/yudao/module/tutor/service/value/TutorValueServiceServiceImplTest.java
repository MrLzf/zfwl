package cn.iocoder.yudao.module.tutor.service.value;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.tutor.controller.app.value.vo.AppTutorValueServiceBuyReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.value.TutorValueServiceConfigDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.value.TutorValueServiceConfigMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.value.TutorValueServiceOrderMapper;
import cn.iocoder.yudao.module.tutor.enums.target.TutorTargetTypeEnum;
import cn.iocoder.yudao.module.tutor.enums.value.TutorValueServiceTypeEnum;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TutorValueServiceServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorValueServiceServiceImpl valueService;

    @Mock
    private TutorValueServiceConfigMapper tutorValueServiceConfigMapper;
    @Mock
    private TutorValueServiceOrderMapper orderMapper;
    @Mock
    private TutorDemandMapper demandMapper;
    @Mock
    private TutorTeacherResumeMapper resumeMapper;
    @Mock
    private MemberUserApi memberUserApi;
    @Mock
    private MemberPointApi memberPointApi;

    @Test
    void buyService_deductsPointAndImmediatelyAppliesTopUntil() {
        TutorValueServiceConfigDO config = TutorValueServiceConfigDO.builder().id(1L)
                .serviceType(TutorValueServiceTypeEnum.TOP.getType()).targetType("demand")
                .pointPrice(30).durationHours(24).status(0).build();
        when(tutorValueServiceConfigMapper.selectById(1L)).thenReturn(config);
        when(demandMapper.selectById(11L)).thenReturn(TutorDemandDO.builder().id(11L).userId(100L).build());
        MemberUserRespDTO user = new MemberUserRespDTO();
        user.setPoint(100);
        when(memberUserApi.getUser(100L)).thenReturn(user);

        AppTutorValueServiceBuyReqVO reqVO = new AppTutorValueServiceBuyReqVO();
        reqVO.setConfigId(1L);
        reqVO.setTargetType(TutorTargetTypeEnum.DEMAND.getType());
        reqVO.setTargetId(11L);
        valueService.buyService(100L, reqVO);

        verify(memberPointApi).reducePoint(eq(100L), eq(30), anyInt(), contains("value-service"));
        verify(demandMapper).updateById(argThat((TutorDemandDO update) -> update.getId().equals(11L)
                && update.getTopUntil().isAfter(LocalDateTime.now())));
    }
}
