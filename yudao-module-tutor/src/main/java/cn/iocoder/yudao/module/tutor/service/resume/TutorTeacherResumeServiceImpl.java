package cn.iocoder.yudao.module.tutor.service.resume;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.publish.vo.AdminTutorPublishAuditReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.resume.vo.AdminTutorTeacherResumePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.resume.vo.AppTutorTeacherResumeSaveReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.square.vo.AppTutorTeacherResumePageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import cn.iocoder.yudao.module.tutor.service.city.TutorCityService;
import cn.iocoder.yudao.module.tutor.service.teacher.TutorTeacherProfileService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.*;

@Service
@Validated
public class TutorTeacherResumeServiceImpl implements TutorTeacherResumeService {

    private static final int MAX_ACTIVE_PUBLISH_COUNT = 3;
    private static final List<Integer> ACTIVE_STATUSES = Arrays.asList(
            TutorPublishStatusEnum.WAIT_AUDIT.getStatus(), TutorPublishStatusEnum.SHOWING.getStatus());

    @Resource
    private TutorTeacherResumeMapper resumeMapper;
    @Resource
    private TutorTeacherProfileService teacherProfileService;
    @Resource
    private TutorCityService cityService;

    @Override
    public TutorTeacherResumeDO createResume(Long userId, AppTutorTeacherResumeSaveReqVO reqVO) {
        validateTeacherCertification(userId);
        validateActivePublishCount(userId);
        TutorCityDO city = cityService.validateCityOpened(reqVO.getCityCode());
        TutorTeacherResumeDO resume = buildResume(userId, city, reqVO)
                .status(TutorPublishStatusEnum.WAIT_AUDIT.getStatus())
                .auditStatus(TutorAuditStatusEnum.WAITING.getStatus())
                .ratingAvg(BigDecimal.ZERO)
                .reviewCount(0)
                .viewCount(0)
                .contactViewCount(0)
                .matchCount(0)
                .build();
        resumeMapper.insert(resume);
        return resume;
    }

    @Override
    public TutorTeacherResumeDO updateResume(Long userId, Long id, AppTutorTeacherResumeSaveReqVO reqVO) {
        validateTeacherCertification(userId);
        TutorTeacherResumeDO resume = validateResumeOwner(userId, id);
        if (!ACTIVE_STATUSES.contains(resume.getStatus())) {
            validateActivePublishCount(userId);
        }
        TutorCityDO city = cityService.validateCityOpened(reqVO.getCityCode());
        TutorTeacherResumeDO updateObj = buildResume(userId, city, reqVO)
                .id(id)
                .status(TutorPublishStatusEnum.WAIT_AUDIT.getStatus())
                .auditStatus(TutorAuditStatusEnum.WAITING.getStatus())
                .build();
        resumeMapper.update(null, new LambdaUpdateWrapper<TutorTeacherResumeDO>()
                .eq(TutorTeacherResumeDO::getId, id)
                .set(TutorTeacherResumeDO::getTitle, updateObj.getTitle())
                .set(TutorTeacherResumeDO::getSubjects, updateObj.getSubjects())
                .set(TutorTeacherResumeDO::getTeachModes, updateObj.getTeachModes())
                .set(TutorTeacherResumeDO::getHourlyPrice, updateObj.getHourlyPrice())
                .set(TutorTeacherResumeDO::getFreeTrialEnabled, updateObj.getFreeTrialEnabled())
                .set(TutorTeacherResumeDO::getFreeTrialMinutes, updateObj.getFreeTrialMinutes())
                .set(TutorTeacherResumeDO::getTeachingExperience, updateObj.getTeachingExperience())
                .set(TutorTeacherResumeDO::getAvailableTimes, updateObj.getAvailableTimes())
                .set(TutorTeacherResumeDO::getCityCode, updateObj.getCityCode())
                .set(TutorTeacherResumeDO::getCityName, updateObj.getCityName())
                .set(TutorTeacherResumeDO::getLongitude, updateObj.getLongitude())
                .set(TutorTeacherResumeDO::getLatitude, updateObj.getLatitude())
                .set(TutorTeacherResumeDO::getServiceRadiusKm, updateObj.getServiceRadiusKm())
                .set(TutorTeacherResumeDO::getContactMobileEncrypt, updateObj.getContactMobileEncrypt())
                .set(TutorTeacherResumeDO::getContactMobileMask, updateObj.getContactMobileMask())
                .set(TutorTeacherResumeDO::getContactWechatEncrypt, updateObj.getContactWechatEncrypt())
                .set(TutorTeacherResumeDO::getContactWechatMask, updateObj.getContactWechatMask())
                .set(TutorTeacherResumeDO::getStatus, updateObj.getStatus())
                .set(TutorTeacherResumeDO::getAuditStatus, updateObj.getAuditStatus())
                .set(TutorTeacherResumeDO::getRejectReason, null));
        return resumeMapper.selectById(id);
    }

    @Override
    public void offlineResume(Long userId, Long id) {
        validateResumeOwner(userId, id);
        resumeMapper.updateById(TutorTeacherResumeDO.builder()
                .id(id)
                .status(TutorPublishStatusEnum.OFFLINE.getStatus())
                .build());
    }

    @Override
    public List<TutorTeacherResumeDO> getMyResumeList(Long userId) {
        return resumeMapper.selectListByUserId(userId);
    }

    @Override
    public PageResult<TutorTeacherResumeDO> getResumePage(AdminTutorTeacherResumePageReqVO reqVO) {
        return resumeMapper.selectPage(reqVO);
    }

    @Override
    @Transactional
    public TutorTeacherResumeDO auditResume(Long auditorId, AdminTutorPublishAuditReqVO reqVO) {
        TutorTeacherResumeDO resume = resumeMapper.selectById(reqVO.getId());
        if (resume == null) {
            throw exception(RESUME_NOT_EXISTS);
        }
        if (TutorAuditStatusEnum.REJECTED.getStatus().equals(reqVO.getAuditStatus())
                && StrUtil.isBlank(reqVO.getRejectReason())) {
            throw exception(PUBLISH_AUDIT_REJECT_REASON_REQUIRED);
        }
        Integer publishStatus = TutorAuditStatusEnum.APPROVED.getStatus().equals(reqVO.getAuditStatus())
                ? TutorPublishStatusEnum.SHOWING.getStatus() : TutorPublishStatusEnum.REJECTED.getStatus();
        resumeMapper.update(null, new LambdaUpdateWrapper<TutorTeacherResumeDO>()
                .eq(TutorTeacherResumeDO::getId, reqVO.getId())
                .set(TutorTeacherResumeDO::getStatus, publishStatus)
                .set(TutorTeacherResumeDO::getAuditStatus, reqVO.getAuditStatus())
                .set(TutorTeacherResumeDO::getRejectReason, TutorAuditStatusEnum.REJECTED.getStatus().equals(reqVO.getAuditStatus())
                        ? reqVO.getRejectReason() : null));
        return resumeMapper.selectById(reqVO.getId());
    }

    @Override
    public PageResult<TutorTeacherResumeDO> getSquareResumePage(AppTutorTeacherResumePageReqVO reqVO) {
        return resumeMapper.selectSquarePage(reqVO);
    }

    @Override
    public TutorTeacherResumeDO getSquareResume(Long id) {
        TutorTeacherResumeDO resume = resumeMapper.selectById(id);
        if (resume == null) {
            throw exception(RESUME_NOT_EXISTS);
        }
        if (!TutorPublishStatusEnum.SHOWING.getStatus().equals(resume.getStatus())
                || !TutorAuditStatusEnum.APPROVED.getStatus().equals(resume.getAuditStatus())) {
            throw exception(PUBLISH_STATUS_NOT_VISIBLE);
        }
        return resume;
    }

    private TutorTeacherResumeDO.TutorTeacherResumeDOBuilder buildResume(Long userId, TutorCityDO city,
                                                                         AppTutorTeacherResumeSaveReqVO reqVO) {
        return TutorTeacherResumeDO.builder()
                .userId(userId)
                .title(reqVO.getTitle())
                .subjects(reqVO.getSubjects())
                .teachModes(reqVO.getTeachModes())
                .hourlyPrice(reqVO.getHourlyPrice())
                .freeTrialEnabled(reqVO.getFreeTrialEnabled())
                .freeTrialMinutes(Boolean.TRUE.equals(reqVO.getFreeTrialEnabled()) ? reqVO.getFreeTrialMinutes() : 0)
                .teachingExperience(reqVO.getTeachingExperience())
                .availableTimes(reqVO.getAvailableTimes())
                .cityCode(city.getCode())
                .cityName(city.getName())
                .longitude(reqVO.getLongitude())
                .latitude(reqVO.getLatitude())
                .serviceRadiusKm(reqVO.getServiceRadiusKm())
                .contactMobileEncrypt(reqVO.getContactMobile())
                .contactMobileMask(DesensitizedUtil.mobilePhone(reqVO.getContactMobile()))
                .contactWechatEncrypt(reqVO.getContactWechat())
                .contactWechatMask(maskWechat(reqVO.getContactWechat()));
    }

    private void validateTeacherCertification(Long userId) {
        TutorTeacherProfileDO teacherProfile = teacherProfileService.getTeacherProfile(userId);
        if (teacherProfile == null) {
            throw exception(TEACHER_PROFILE_NOT_EXISTS);
        }
        if (!TutorAuditStatusEnum.APPROVED.getStatus().equals(teacherProfile.getCertificationStatus())) {
            throw exception(CERTIFICATION_NOT_APPROVED);
        }
    }

    private TutorTeacherResumeDO validateResumeOwner(Long userId, Long id) {
        TutorTeacherResumeDO resume = resumeMapper.selectById(id);
        if (resume == null) {
            throw exception(RESUME_NOT_EXISTS);
        }
        if (!Objects.equals(userId, resume.getUserId())) {
            throw exception(PUBLISH_OPERATION_FORBIDDEN);
        }
        return resume;
    }

    private void validateActivePublishCount(Long userId) {
        Long count = resumeMapper.selectCountByUserIdAndStatuses(userId, ACTIVE_STATUSES);
        if (count != null && count >= MAX_ACTIVE_PUBLISH_COUNT) {
            throw exception(PUBLISH_COUNT_EXCEED);
        }
    }

    private String maskWechat(String wechat) {
        if (StrUtil.isBlank(wechat)) {
            return null;
        }
        if (wechat.length() <= 4) {
            return wechat.charAt(0) + "***";
        }
        return wechat.substring(0, 2) + "***" + wechat.substring(wechat.length() - 2);
    }

}
