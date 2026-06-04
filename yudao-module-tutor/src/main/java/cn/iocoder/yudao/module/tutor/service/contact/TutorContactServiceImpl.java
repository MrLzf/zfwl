package cn.iocoder.yudao.module.tutor.service.contact;

import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.contact.vo.AdminTutorContactPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.common.vo.AppTutorTargetReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.contact.vo.AppTutorContactRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.contact.TutorContactViewRecordDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.match.TutorMatchRecordDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.contact.TutorContactViewRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.match.TutorMatchRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.enums.match.TutorMatchStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.target.TutorTargetTypeEnum;
import cn.iocoder.yudao.module.tutor.service.demand.TutorDemandService;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import cn.iocoder.yudao.module.tutor.service.resume.TutorTeacherResumeService;
import cn.iocoder.yudao.module.tutor.service.vip.TutorVipService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.CONTACT_POINT_NOT_ENOUGH;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.CONTACT_TARGET_NOT_EXISTS;

@Service
@Validated
public class TutorContactServiceImpl implements TutorContactService {

    private static final int VIEW_CONTACT_POINT_COST = 10;
    private static final String SAFETY_TIP = "请核验对方身份，线下见面选择公共场所，未确认前不要提前转账。";
    private static final String CONTACT_LOCK_KEY_PREFIX = "tutor:contact:view:";

    @Resource
    private TutorContactViewRecordMapper contactViewRecordMapper;
    @Resource
    private TutorMatchRecordMapper matchRecordMapper;
    @Resource
    private TutorDemandMapper demandMapper;
    @Resource
    private TutorTeacherResumeMapper resumeMapper;
    @Resource
    private TutorDemandService demandService;
    @Resource
    private TutorTeacherResumeService resumeService;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private MemberPointApi memberPointApi;
    @Resource
    private TutorNotifyService tutorNotifyService;
    @Resource
    private TutorVipService vipService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public AppTutorContactRespVO viewContact(Long viewerUserId, AppTutorTargetReqVO reqVO) {
        RLock lock = redissonClient.getLock(buildContactLockKey(viewerUserId, reqVO.getTargetType(), reqVO.getTargetId()));
        lock.lock(10, TimeUnit.SECONDS);
        try {
            return transactionTemplate.execute(status -> viewContact0(viewerUserId, reqVO));
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private AppTutorContactRespVO viewContact0(Long viewerUserId, AppTutorTargetReqVO reqVO) {
        LocalDateTime now = LocalDateTime.now();
        TutorContactViewRecordDO reusable = contactViewRecordMapper.selectReusable(viewerUserId, reqVO.getTargetType(), reqVO.getTargetId(), now);
        if (TutorTargetTypeEnum.isDemand(reqVO.getTargetType())) {
            TutorDemandDO demand = demandService.getSquareDemand(reqVO.getTargetId());
            validateNotSelf(viewerUserId, demand.getUserId());
            int pointCost = 0;
            if (reusable == null) {
                pointCost = deductPoint(viewerUserId, reqVO);
                createRecord(viewerUserId, demand.getUserId(), reqVO, pointCost);
                demandMapper.updateContactViewCountIncr(demand.getId());
            }
            createOrGetMatchForDemand(demand, viewerUserId);
            tutorNotifyService.sendContactViewed(viewerUserId, demand.getUserId(), getNickname(viewerUserId),
                    getNickname(demand.getUserId()), demand.getTitle(), reusable != null, reqVO.getTargetType(), reqVO.getTargetId());
            return buildResp(reqVO, demand.getUserId(), demand.getContactMobileEncrypt(), demand.getContactWechatEncrypt(), reusable, pointCost);
        }
        if (TutorTargetTypeEnum.isResume(reqVO.getTargetType())) {
            TutorTeacherResumeDO resume = resumeService.getSquareResume(reqVO.getTargetId());
            validateNotSelf(viewerUserId, resume.getUserId());
            int pointCost = 0;
            if (reusable == null) {
                pointCost = deductPoint(viewerUserId, reqVO);
                createRecord(viewerUserId, resume.getUserId(), reqVO, pointCost);
                resumeMapper.updateContactViewCountIncr(resume.getId());
            }
            createOrGetMatchForResume(resume, viewerUserId);
            tutorNotifyService.sendContactViewed(viewerUserId, resume.getUserId(), getNickname(viewerUserId),
                    getNickname(resume.getUserId()), resume.getTitle(), reusable != null, reqVO.getTargetType(), reqVO.getTargetId());
            return buildResp(reqVO, resume.getUserId(), resume.getContactMobileEncrypt(), resume.getContactWechatEncrypt(), reusable, pointCost);
        }
        throw exception(CONTACT_TARGET_NOT_EXISTS);
    }

    @Override
    public AppTutorContactRespVO getOwnerContact(Long viewerUserId, String targetType, Long targetId) {
        if (viewerUserId == null) {
            return null;
        }
        if (TutorTargetTypeEnum.isDemand(targetType)) {
            TutorDemandDO demand = demandMapper.selectById(targetId);
            if (demand == null || !Objects.equals(viewerUserId, demand.getUserId())) {
                return null;
            }
            return buildResp(targetType, targetId, demand.getUserId(), demand.getContactMobileEncrypt(),
                    demand.getContactWechatEncrypt(), true);
        }
        if (TutorTargetTypeEnum.isResume(targetType)) {
            TutorTeacherResumeDO resume = resumeMapper.selectById(targetId);
            if (resume == null || !Objects.equals(viewerUserId, resume.getUserId())) {
                return null;
            }
            return buildResp(targetType, targetId, resume.getUserId(), resume.getContactMobileEncrypt(),
                    resume.getContactWechatEncrypt(), true);
        }
        return null;
    }

    @Override
    public AppTutorContactRespVO getReusableContact(Long viewerUserId, String targetType, Long targetId) {
        if (viewerUserId == null) {
            return null;
        }
        TutorContactViewRecordDO reusable = contactViewRecordMapper.selectReusable(viewerUserId, targetType, targetId, LocalDateTime.now());
        if (reusable == null) {
            return null;
        }
        if (TutorTargetTypeEnum.isDemand(targetType)) {
            TutorDemandDO demand = demandMapper.selectById(targetId);
            if (demand == null || !Objects.equals(demand.getUserId(), reusable.getTargetUserId())) {
                return null;
            }
            return buildResp(targetType, targetId, demand.getUserId(), demand.getContactMobileEncrypt(),
                    demand.getContactWechatEncrypt(), true);
        }
        if (TutorTargetTypeEnum.isResume(targetType)) {
            TutorTeacherResumeDO resume = resumeMapper.selectById(targetId);
            if (resume == null || !Objects.equals(resume.getUserId(), reusable.getTargetUserId())) {
                return null;
            }
            return buildResp(targetType, targetId, resume.getUserId(), resume.getContactMobileEncrypt(),
                    resume.getContactWechatEncrypt(), true);
        }
        return null;
    }

    @Override
    public List<TutorContactViewRecordDO> getMyContactRecordList(Long viewerUserId) {
        return contactViewRecordMapper.selectListByViewerUserId(viewerUserId);
    }

    @Override
    public PageResult<TutorContactViewRecordDO> getContactPage(AdminTutorContactPageReqVO reqVO) {
        return contactViewRecordMapper.selectPage(reqVO);
    }

    private int deductPoint(Long viewerUserId, AppTutorTargetReqVO reqVO) {
        int pointCost = calculatePointCost(viewerUserId);
        MemberUserRespDTO user = memberUserApi.getUser(viewerUserId);
        if (user == null || user.getPoint() == null || user.getPoint() < pointCost) {
            throw exception(CONTACT_POINT_NOT_ENOUGH);
        }
        memberPointApi.reducePoint(viewerUserId, pointCost,
                MemberPointBizTypeEnum.TUTOR_VIEW_CONTACT.getType(), reqVO.getTargetType() + ":" + reqVO.getTargetId());
        tutorNotifyService.sendPointChanged(viewerUserId, "查看联系方式", -pointCost,
                user.getPoint() - pointCost, "point", "point_records",
                reqVO.getTargetType() + ":" + reqVO.getTargetId(), reqVO.getTargetType(), reqVO.getTargetId());
        return pointCost;
    }

    private void createRecord(Long viewerUserId, Long targetUserId, AppTutorTargetReqVO reqVO, int pointCost) {
        contactViewRecordMapper.insert(TutorContactViewRecordDO.builder()
                .viewerUserId(viewerUserId)
                .targetType(reqVO.getTargetType())
                .targetId(reqVO.getTargetId())
                .targetUserId(targetUserId)
                .pointCost(pointCost)
                .freeReuseUntil(LocalDateTime.now().plusDays(30))
                .viewedMobile(true)
                .viewedWechat(true)
                .build());
    }

    private void createOrGetMatchForDemand(TutorDemandDO demand, Long teacherUserId) {
        TutorMatchRecordDO match = matchRecordMapper.selectByDemandAndTeacher(demand.getId(), teacherUserId);
        if (match != null) {
            return;
        }
        matchRecordMapper.insert(TutorMatchRecordDO.builder()
                .demandId(demand.getId()).parentUserId(demand.getUserId()).teacherUserId(teacherUserId)
                .source("contact_view").status(TutorMatchStatusEnum.CONTACT_VIEWED.getStatus()).build());
    }

    private void createOrGetMatchForResume(TutorTeacherResumeDO resume, Long parentUserId) {
        TutorMatchRecordDO match = matchRecordMapper.selectByResumeAndParent(resume.getId(), parentUserId);
        if (match != null) {
            return;
        }
        matchRecordMapper.insert(TutorMatchRecordDO.builder()
                .resumeId(resume.getId()).parentUserId(parentUserId).teacherUserId(resume.getUserId())
                .source("contact_view").status(TutorMatchStatusEnum.CONTACT_VIEWED.getStatus()).build());
    }

    private AppTutorContactRespVO buildResp(AppTutorTargetReqVO reqVO, Long targetUserId, String mobile, String wechat, boolean reused) {
        return buildResp(reqVO.getTargetType(), reqVO.getTargetId(), targetUserId, mobile, wechat, null, reused);
    }

    private AppTutorContactRespVO buildResp(String targetType, Long targetId, Long targetUserId, String mobile, String wechat,
                                            boolean reused) {
        return buildResp(targetType, targetId, targetUserId, mobile, wechat, null, reused);
    }

    private AppTutorContactRespVO buildResp(AppTutorTargetReqVO reqVO, Long targetUserId, String mobile, String wechat,
                                            TutorContactViewRecordDO reusable) {
        return buildResp(reqVO.getTargetType(), reqVO.getTargetId(), targetUserId, mobile, wechat, reusable, reusable != null);
    }

    private AppTutorContactRespVO buildResp(AppTutorTargetReqVO reqVO, Long targetUserId, String mobile, String wechat,
                                            TutorContactViewRecordDO reusable, int pointCost) {
        return buildResp(reqVO.getTargetType(), reqVO.getTargetId(), targetUserId, mobile, wechat, reusable,
                reusable != null, pointCost);
    }

    private AppTutorContactRespVO buildResp(String targetType, Long targetId, Long targetUserId, String mobile, String wechat,
                                            TutorContactViewRecordDO reusable, boolean reused) {
        return buildResp(targetType, targetId, targetUserId, mobile, wechat, reusable, reused,
                reused ? 0 : VIEW_CONTACT_POINT_COST);
    }

    private AppTutorContactRespVO buildResp(String targetType, Long targetId, Long targetUserId, String mobile, String wechat,
                                            TutorContactViewRecordDO reusable, boolean reused, int pointCost) {
        if (reusable != null && reusable.getPointCost() != null) {
            pointCost = 0;
        }
        return AppTutorContactRespVO.builder()
                .targetType(targetType).targetId(targetId).targetUserId(targetUserId)
                .mobile(mobile).wechat(wechat).pointCost(pointCost)
                .reused(reused).safetyTip(SAFETY_TIP).build();
    }

    private int calculatePointCost(Long viewerUserId) {
        return vipService == null ? VIEW_CONTACT_POINT_COST
                : vipService.calculateContactPointCost(viewerUserId, VIEW_CONTACT_POINT_COST);
    }

    private void validateNotSelf(Long viewerUserId, Long targetUserId) {
        if (Objects.equals(viewerUserId, targetUserId)) {
            throw exception(CONTACT_TARGET_NOT_EXISTS);
        }
    }

    private String getNickname(Long userId) {
        MemberUserRespDTO user = memberUserApi.getUser(userId);
        return user == null || user.getNickname() == null ? "" : user.getNickname();
    }

    private String buildContactLockKey(Long viewerUserId, String targetType, Long targetId) {
        return CONTACT_LOCK_KEY_PREFIX + viewerUserId + ":" + targetType + ":" + targetId;
    }

}
