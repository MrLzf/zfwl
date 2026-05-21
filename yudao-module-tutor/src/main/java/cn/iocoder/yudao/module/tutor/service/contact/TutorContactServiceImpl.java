package cn.iocoder.yudao.module.tutor.service.contact;

import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
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
import cn.iocoder.yudao.module.tutor.service.resume.TutorTeacherResumeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.CONTACT_POINT_NOT_ENOUGH;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.CONTACT_TARGET_NOT_EXISTS;

@Service
@Validated
public class TutorContactServiceImpl implements TutorContactService {

    private static final int VIEW_CONTACT_POINT_COST = 10;
    private static final String SAFETY_TIP = "请核验对方身份，线下见面选择公共场所，未确认前不要提前转账。";

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppTutorContactRespVO viewContact(Long viewerUserId, AppTutorTargetReqVO reqVO) {
        LocalDateTime now = LocalDateTime.now();
        TutorContactViewRecordDO reusable = contactViewRecordMapper.selectReusable(viewerUserId, reqVO.getTargetType(), reqVO.getTargetId(), now);
        if (TutorTargetTypeEnum.isDemand(reqVO.getTargetType())) {
            TutorDemandDO demand = demandService.getSquareDemand(reqVO.getTargetId());
            validateNotSelf(viewerUserId, demand.getUserId());
            if (reusable == null) {
                deductPoint(viewerUserId, reqVO);
                createRecord(viewerUserId, demand.getUserId(), reqVO);
                demandMapper.updateContactViewCountIncr(demand.getId());
            }
            createOrGetMatchForDemand(demand, viewerUserId);
            return buildResp(reqVO, demand.getUserId(), demand.getContactMobileEncrypt(), demand.getContactWechatEncrypt(), reusable != null);
        }
        if (TutorTargetTypeEnum.isResume(reqVO.getTargetType())) {
            TutorTeacherResumeDO resume = resumeService.getSquareResume(reqVO.getTargetId());
            validateNotSelf(viewerUserId, resume.getUserId());
            if (reusable == null) {
                deductPoint(viewerUserId, reqVO);
                createRecord(viewerUserId, resume.getUserId(), reqVO);
                resumeMapper.updateContactViewCountIncr(resume.getId());
            }
            createOrGetMatchForResume(resume, viewerUserId);
            return buildResp(reqVO, resume.getUserId(), resume.getContactMobileEncrypt(), resume.getContactWechatEncrypt(), reusable != null);
        }
        throw exception(CONTACT_TARGET_NOT_EXISTS);
    }

    @Override
    public List<TutorContactViewRecordDO> getMyContactRecordList(Long viewerUserId) {
        return contactViewRecordMapper.selectListByViewerUserId(viewerUserId);
    }

    private void deductPoint(Long viewerUserId, AppTutorTargetReqVO reqVO) {
        MemberUserRespDTO user = memberUserApi.getUser(viewerUserId);
        if (user == null || user.getPoint() == null || user.getPoint() < VIEW_CONTACT_POINT_COST) {
            throw exception(CONTACT_POINT_NOT_ENOUGH);
        }
        memberPointApi.reducePoint(viewerUserId, VIEW_CONTACT_POINT_COST,
                MemberPointBizTypeEnum.TUTOR_VIEW_CONTACT.getType(), reqVO.getTargetType() + ":" + reqVO.getTargetId());
    }

    private void createRecord(Long viewerUserId, Long targetUserId, AppTutorTargetReqVO reqVO) {
        contactViewRecordMapper.insert(TutorContactViewRecordDO.builder()
                .viewerUserId(viewerUserId)
                .targetType(reqVO.getTargetType())
                .targetId(reqVO.getTargetId())
                .targetUserId(targetUserId)
                .pointCost(VIEW_CONTACT_POINT_COST)
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
        return AppTutorContactRespVO.builder()
                .targetType(reqVO.getTargetType()).targetId(reqVO.getTargetId()).targetUserId(targetUserId)
                .mobile(mobile).wechat(wechat).pointCost(reused ? 0 : VIEW_CONTACT_POINT_COST)
                .reused(reused).safetyTip(SAFETY_TIP).build();
    }

    private void validateNotSelf(Long viewerUserId, Long targetUserId) {
        if (Objects.equals(viewerUserId, targetUserId)) {
            throw exception(CONTACT_TARGET_NOT_EXISTS);
        }
    }

}
