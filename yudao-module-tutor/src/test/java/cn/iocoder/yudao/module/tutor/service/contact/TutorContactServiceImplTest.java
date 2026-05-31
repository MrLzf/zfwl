package cn.iocoder.yudao.module.tutor.service.contact;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.tutor.controller.app.common.vo.AppTutorTargetReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.contact.vo.AppTutorContactRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.contact.TutorContactViewRecordDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.contact.TutorContactViewRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.match.TutorMatchRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.enums.target.TutorTargetTypeEnum;
import cn.iocoder.yudao.module.tutor.service.demand.TutorDemandService;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import cn.iocoder.yudao.module.tutor.service.resume.TutorTeacherResumeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TutorContactServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorContactServiceImpl contactService;

    @Mock
    private TutorContactViewRecordMapper contactViewRecordMapper;
    @Mock
    private TutorMatchRecordMapper matchRecordMapper;
    @Mock
    private TutorDemandMapper demandMapper;
    @Mock
    private TutorTeacherResumeMapper resumeMapper;
    @Mock
    private TutorDemandService demandService;
    @Mock
    private TutorTeacherResumeService resumeService;
    @Mock
    private MemberUserApi memberUserApi;
    @Mock
    private MemberPointApi memberPointApi;
    @Mock
    private TutorNotifyService tutorNotifyService;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private TransactionTemplate transactionTemplate;
    @Mock
    private RLock lock;

    @Test
    void viewContact_firstView_locksAndDeductsPoint() {
        AppTutorTargetReqVO reqVO = new AppTutorTargetReqVO();
        reqVO.setTargetType(TutorTargetTypeEnum.DEMAND.getType());
        reqVO.setTargetId(11L);
        TutorDemandDO demand = TutorDemandDO.builder()
                .id(11L).userId(200L).title("高一数学辅导")
                .contactMobileEncrypt("13800000000").contactWechatEncrypt("wechat").build();
        MemberUserRespDTO viewer = new MemberUserRespDTO();
        viewer.setId(100L);
        viewer.setPoint(10);
        viewer.setNickname("李家长");
        MemberUserRespDTO owner = new MemberUserRespDTO();
        owner.setId(200L);
        owner.setNickname("王老师");

        when(redissonClient.getLock("tutor:contact:view:100:demand:11")).thenReturn(lock);
        when(lock.isHeldByCurrentThread()).thenReturn(true);
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            TransactionCallback<?> callback = invocation.getArgument(0);
            return callback.doInTransaction(null);
        });
        when(contactViewRecordMapper.selectReusable(eq(100L), eq("demand"), eq(11L), any(LocalDateTime.class))).thenReturn(null);
        when(demandService.getSquareDemand(11L)).thenReturn(demand);
        when(memberUserApi.getUser(100L)).thenReturn(viewer);
        when(memberUserApi.getUser(200L)).thenReturn(owner);

        AppTutorContactRespVO respVO = contactService.viewContact(100L, reqVO);

        assertEquals("13800000000", respVO.getMobile());
        assertEquals(10, respVO.getPointCost());
        assertEquals(false, respVO.getReused());
        verify(lock).lock(10, TimeUnit.SECONDS);
        verify(lock).unlock();
        verify(memberPointApi).reducePoint(eq(100L), eq(10),
                eq(MemberPointBizTypeEnum.TUTOR_VIEW_CONTACT.getType()), eq("demand:11"));
        verify(tutorNotifyService).sendPointChanged(100L, "查看联系方式", -10, 0,
                "point", "point_records", "demand:11", "demand", 11L);
        verify(tutorNotifyService).sendContactViewed(100L, 200L, "李家长", "王老师", "高一数学辅导",
                false, "demand", 11L);
        verify(contactViewRecordMapper).insert(any(TutorContactViewRecordDO.class));
        verify(demandMapper).updateContactViewCountIncr(11L);
    }

    @Test
    void viewContact_reusableView_sendsContactNotificationsWithoutDeductingPoint() {
        AppTutorTargetReqVO reqVO = new AppTutorTargetReqVO();
        reqVO.setTargetType(TutorTargetTypeEnum.DEMAND.getType());
        reqVO.setTargetId(11L);
        TutorDemandDO demand = TutorDemandDO.builder()
                .id(11L).userId(200L).title("高一数学辅导")
                .contactMobileEncrypt("13800000000").contactWechatEncrypt("wechat").build();
        MemberUserRespDTO viewer = new MemberUserRespDTO();
        viewer.setId(100L);
        viewer.setNickname("李家长");
        MemberUserRespDTO owner = new MemberUserRespDTO();
        owner.setId(200L);
        owner.setNickname("王老师");

        when(redissonClient.getLock("tutor:contact:view:100:demand:11")).thenReturn(lock);
        when(lock.isHeldByCurrentThread()).thenReturn(true);
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            TransactionCallback<?> callback = invocation.getArgument(0);
            return callback.doInTransaction(null);
        });
        when(contactViewRecordMapper.selectReusable(eq(100L), eq("demand"), eq(11L), any(LocalDateTime.class)))
                .thenReturn(TutorContactViewRecordDO.builder().targetUserId(200L).build());
        when(demandService.getSquareDemand(11L)).thenReturn(demand);
        when(memberUserApi.getUser(100L)).thenReturn(viewer);
        when(memberUserApi.getUser(200L)).thenReturn(owner);

        AppTutorContactRespVO respVO = contactService.viewContact(100L, reqVO);

        assertTrue(respVO.getReused());
        verify(memberPointApi, never()).reducePoint(anyLong(), anyInt(), anyInt(), anyString());
        verify(tutorNotifyService).sendContactViewed(100L, 200L, "李家长", "王老师", "高一数学辅导",
                true, "demand", 11L);
    }

    @Test
    void getReusableContact_whenRecordValid_returnsUnlockedContact() {
        when(contactViewRecordMapper.selectReusable(eq(100L), eq("demand"), eq(11L), any(LocalDateTime.class)))
                .thenReturn(TutorContactViewRecordDO.builder().targetUserId(200L).build());
        when(demandMapper.selectById(11L)).thenReturn(TutorDemandDO.builder()
                .id(11L).userId(200L).contactMobileEncrypt("13800000000").contactWechatEncrypt("wechat").build());

        AppTutorContactRespVO respVO = contactService.getReusableContact(100L, "demand", 11L);

        assertTrue(respVO.getReused());
        assertEquals(0, respVO.getPointCost());
        assertEquals("13800000000", respVO.getMobile());
        verify(memberPointApi, never()).reducePoint(anyLong(), anyInt(), anyInt(), anyString());
    }
}
