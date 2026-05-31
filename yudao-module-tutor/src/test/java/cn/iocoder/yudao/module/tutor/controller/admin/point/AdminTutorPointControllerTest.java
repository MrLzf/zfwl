package cn.iocoder.yudao.module.tutor.controller.admin.point;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.tutor.controller.admin.point.vo.AdminTutorPointAdjustReqVO;
import cn.iocoder.yudao.module.tutor.dal.mysql.point.TutorPointAdjustRecordMapper;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminTutorPointControllerTest extends BaseMockitoUnitTest {

    @InjectMocks
    private AdminTutorPointController controller;

    @Mock
    private MemberPointApi memberPointApi;
    @Mock
    private MemberUserApi memberUserApi;
    @Mock
    private TutorPointAdjustRecordMapper pointAdjustRecordMapper;
    @Mock
    private TutorNotifyService tutorNotifyService;

    @Test
    void adjustPoint_whenBalanceAvailable_sendsEnrichedPointNotification() {
        AdminTutorPointAdjustReqVO reqVO = new AdminTutorPointAdjustReqVO();
        reqVO.setUserId(100L);
        reqVO.setPoint(10);
        reqVO.setRemark("运营补偿");
        MemberUserRespDTO user = new MemberUserRespDTO();
        user.setPoint(50);
        when(memberUserApi.getUser(100L)).thenReturn(user);

        try (MockedStatic<SecurityFrameworkUtils> utils = mockStatic(SecurityFrameworkUtils.class)) {
            utils.when(SecurityFrameworkUtils::getLoginUserId).thenReturn(9L);
            controller.adjustPoint(reqVO);
        }

        verify(tutorNotifyService).sendPointChanged(100L, "后台积分调整", 10, 50,
                "point", "adjust", "tutor:9:null", null, null);
    }

}
