package cn.iocoder.yudao.module.member.service.signin;

import cn.iocoder.yudao.module.member.dal.dataobject.signin.MemberSignInConfigDO;
import cn.iocoder.yudao.module.member.dal.mysql.signin.MemberSignInRecordMapper;
import cn.iocoder.yudao.module.member.service.level.MemberLevelService;
import cn.iocoder.yudao.module.member.service.point.MemberPointRecordService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.ArrayList;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.SIGN_IN_CONFIG_NOT_EXISTS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberSignInRecordServiceImplTest {

    @InjectMocks
    private MemberSignInRecordServiceImpl signInRecordService;

    @Mock
    private MemberSignInRecordMapper signInRecordMapper;
    @Mock
    private MemberSignInConfigService signInConfigService;
    @Mock
    private MemberPointRecordService pointRecordService;
    @Mock
    private MemberLevelService memberLevelService;
    @Mock
    private MemberUserService memberUserService;

    @Test
    void testCreateSignRecord_noEnabledConfig() {
        Long userId = 6L;
        when(signInConfigService.getSignInConfigList(0)).thenReturn(Collections.emptyList());

        assertServiceException(() -> signInRecordService.createSignRecord(userId), SIGN_IN_CONFIG_NOT_EXISTS);
    }

    @Test
    void testCreateSignRecord_missingFirstDayConfig() {
        Long userId = 6L;
        MemberSignInConfigDO config = new MemberSignInConfigDO().setDay(7).setPoint(5).setExperience(10);
        when(signInConfigService.getSignInConfigList(0)).thenReturn(new ArrayList<>(Collections.singletonList(config)));

        assertServiceException(() -> signInRecordService.createSignRecord(userId), SIGN_IN_CONFIG_NOT_EXISTS);
    }

}
