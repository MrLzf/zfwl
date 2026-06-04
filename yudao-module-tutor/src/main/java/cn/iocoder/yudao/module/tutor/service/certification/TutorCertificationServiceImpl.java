package cn.iocoder.yudao.module.tutor.service.certification;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.certification.vo.AdminTutorCertificationAuditReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.certification.vo.AdminTutorCertificationPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.certification.vo.AppTutorCertificationSubmitReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.certification.TutorCertificationDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.teacher.TutorTeacherProfileDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.certification.TutorCertificationMapper;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import cn.iocoder.yudao.module.tutor.service.security.TutorContentSecurityService;
import cn.iocoder.yudao.module.tutor.service.teacher.TutorTeacherProfileService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.*;

@Service
@Validated
public class TutorCertificationServiceImpl implements TutorCertificationService {

    @Resource
    private TutorCertificationMapper certificationMapper;
    @Resource
    private TutorTeacherProfileService teacherProfileService;
    @Resource
    private TutorNotifyService tutorNotifyService;
    @Resource
    private TutorContentSecurityService contentSecurityService;

    @Override
    public TutorCertificationDO getCertification(Long userId) {
        return certificationMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public TutorCertificationDO submitCertification(Long userId, AppTutorCertificationSubmitReqVO reqVO) {
        if (contentSecurityService != null) {
            contentSecurityService.validateTexts("certification", Arrays.asList(reqVO.getRealName(),
                    reqVO.getEducationFileUrl(), reqVO.getTeacherCertFileUrl()));
        }
        TutorTeacherProfileDO teacherProfile = teacherProfileService.getOrCreateTeacherProfile(userId);
        TutorCertificationDO certification = certificationMapper.selectByUserId(userId);
        if (certification != null && TutorAuditStatusEnum.WAITING.getStatus().equals(certification.getStatus())) {
            throw exception(CERTIFICATION_PENDING);
        }

        if (certification == null) {
            certification = TutorCertificationDO.builder()
                    .userId(userId)
                    .teacherProfileId(teacherProfile.getId())
                    .realName(reqVO.getRealName())
                    .idCardNoEncrypt(reqVO.getIdCardNo())
                    .idCardNoMask(maskIdCard(reqVO.getIdCardNo()))
                    .educationFileUrl(reqVO.getEducationFileUrl())
                    .teacherCertFileUrl(reqVO.getTeacherCertFileUrl())
                    .status(TutorAuditStatusEnum.WAITING.getStatus())
                    .build();
            certificationMapper.insert(certification);
        } else {
            certificationMapper.update(null, new LambdaUpdateWrapper<TutorCertificationDO>()
                    .eq(TutorCertificationDO::getId, certification.getId())
                    .set(TutorCertificationDO::getTeacherProfileId, teacherProfile.getId())
                    .set(TutorCertificationDO::getRealName, reqVO.getRealName())
                    .set(TutorCertificationDO::getIdCardNoEncrypt, reqVO.getIdCardNo())
                    .set(TutorCertificationDO::getIdCardNoMask, maskIdCard(reqVO.getIdCardNo()))
                    .set(TutorCertificationDO::getEducationFileUrl, reqVO.getEducationFileUrl())
                    .set(TutorCertificationDO::getTeacherCertFileUrl, reqVO.getTeacherCertFileUrl())
                    .set(TutorCertificationDO::getStatus, TutorAuditStatusEnum.WAITING.getStatus())
                    .set(TutorCertificationDO::getRejectReason, null)
                    .set(TutorCertificationDO::getAuditorId, null)
                    .set(TutorCertificationDO::getAuditTime, null));
            certification = certificationMapper.selectById(certification.getId());
        }
        teacherProfileService.updateCertificationStatus(userId, TutorAuditStatusEnum.WAITING.getStatus());
        return certification;
    }

    @Override
    public PageResult<TutorCertificationDO> getCertificationPage(AdminTutorCertificationPageReqVO reqVO) {
        return certificationMapper.selectPage(reqVO);
    }

    @Override
    @Transactional
    public TutorCertificationDO auditCertification(Long auditorId, AdminTutorCertificationAuditReqVO reqVO) {
        TutorCertificationDO certification = certificationMapper.selectById(reqVO.getId());
        if (certification == null) {
            throw exception(CERTIFICATION_NOT_EXISTS);
        }
        if (TutorAuditStatusEnum.REJECTED.getStatus().equals(reqVO.getStatus())
                && StrUtil.isBlank(reqVO.getRejectReason())) {
            throw exception(CERTIFICATION_REJECT_REASON_REQUIRED);
        }
        certificationMapper.update(null, new LambdaUpdateWrapper<TutorCertificationDO>()
                .eq(TutorCertificationDO::getId, reqVO.getId())
                .set(TutorCertificationDO::getStatus, reqVO.getStatus())
                .set(TutorCertificationDO::getRejectReason, TutorAuditStatusEnum.REJECTED.getStatus().equals(reqVO.getStatus())
                        ? reqVO.getRejectReason() : null)
                .set(TutorCertificationDO::getAuditorId, auditorId)
                .set(TutorCertificationDO::getAuditTime, LocalDateTime.now()));
        teacherProfileService.updateCertificationStatus(certification.getUserId(), reqVO.getStatus());
        TutorCertificationDO updatedCertification = certificationMapper.selectById(reqVO.getId());
        tutorNotifyService.sendCertificationAuditResult(updatedCertification.getUserId(), updatedCertification.getStatus(),
                updatedCertification.getRejectReason());
        return updatedCertification;
    }

    private String maskIdCard(String idCardNo) {
        return DesensitizedUtil.idCardNum(idCardNo, 6, 4);
    }

}
