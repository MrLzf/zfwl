package cn.iocoder.yudao.module.tutor.service.demand;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.demand.vo.AdminTutorDemandPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.publish.vo.AdminTutorPublishAuditReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.demand.vo.AppTutorDemandSaveReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.parent.vo.AppTutorParentProfileSaveReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.square.vo.AppTutorDemandPageReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import cn.iocoder.yudao.module.tutor.service.city.TutorCityService;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import cn.iocoder.yudao.module.tutor.service.parent.TutorParentProfileService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.*;

@Service
@Validated
public class TutorDemandServiceImpl implements TutorDemandService {

    private static final int MAX_ACTIVE_PUBLISH_COUNT = 3;
    private static final List<Integer> ACTIVE_STATUSES = Arrays.asList(
            TutorPublishStatusEnum.WAIT_AUDIT.getStatus(), TutorPublishStatusEnum.SHOWING.getStatus());

    @Resource
    private TutorDemandMapper demandMapper;
    @Resource
    private TutorParentProfileService parentProfileService;
    @Resource
    private TutorCityService cityService;
    @Resource
    private TutorNotifyService tutorNotifyService;

    @Override
    @Transactional
    public TutorDemandDO createDemand(Long userId, AppTutorDemandSaveReqVO reqVO) {
        saveParentProfileFromDemand(userId, reqVO);
        validateActivePublishCount(userId);
        TutorCityDO city = cityService.validateCityOpened(reqVO.getCityCode());
        TutorDemandDO demand = buildDemand(userId, city, reqVO)
                .status(TutorPublishStatusEnum.WAIT_AUDIT.getStatus())
                .auditStatus(TutorAuditStatusEnum.WAITING.getStatus())
                .viewCount(0)
                .contactViewCount(0)
                .matchCount(0)
                .expireTime(LocalDateTime.now().plusDays(30))
                .build();
        demandMapper.insert(demand);
        return demand;
    }

    @Override
    @Transactional
    public TutorDemandDO updateDemand(Long userId, Long id, AppTutorDemandSaveReqVO reqVO) {
        saveParentProfileFromDemand(userId, reqVO);
        TutorDemandDO demand = validateDemandOwner(userId, id);
        if (!ACTIVE_STATUSES.contains(demand.getStatus())) {
            validateActivePublishCount(userId);
        }
        TutorCityDO city = cityService.validateCityOpened(reqVO.getCityCode());
        TutorDemandDO updateObj = buildDemand(userId, city, reqVO)
                .id(id)
                .status(TutorPublishStatusEnum.WAIT_AUDIT.getStatus())
                .auditStatus(TutorAuditStatusEnum.WAITING.getStatus())
                .expireTime(LocalDateTime.now().plusDays(30))
                .build();
        demandMapper.update(null, new LambdaUpdateWrapper<TutorDemandDO>()
                .eq(TutorDemandDO::getId, id)
                .set(TutorDemandDO::getTitle, updateObj.getTitle())
                .set(TutorDemandDO::getGrade, updateObj.getGrade())
                .set(TutorDemandDO::getSubjects, updateObj.getSubjects())
                .set(TutorDemandDO::getTeachMode, updateObj.getTeachMode())
                .set(TutorDemandDO::getBudgetMin, updateObj.getBudgetMin())
                .set(TutorDemandDO::getBudgetMax, updateObj.getBudgetMax())
                .set(TutorDemandDO::getDescription, updateObj.getDescription())
                .set(TutorDemandDO::getCityCode, updateObj.getCityCode())
                .set(TutorDemandDO::getCityName, updateObj.getCityName())
                .set(TutorDemandDO::getLongitude, updateObj.getLongitude())
                .set(TutorDemandDO::getLatitude, updateObj.getLatitude())
                .set(TutorDemandDO::getDistanceVisible, updateObj.getDistanceVisible())
                .set(TutorDemandDO::getContactMobileEncrypt, updateObj.getContactMobileEncrypt())
                .set(TutorDemandDO::getContactMobileMask, updateObj.getContactMobileMask())
                .set(TutorDemandDO::getContactWechatEncrypt, updateObj.getContactWechatEncrypt())
                .set(TutorDemandDO::getContactWechatMask, updateObj.getContactWechatMask())
                .set(TutorDemandDO::getStatus, updateObj.getStatus())
                .set(TutorDemandDO::getAuditStatus, updateObj.getAuditStatus())
                .set(TutorDemandDO::getRejectReason, null)
                .set(TutorDemandDO::getExpireTime, updateObj.getExpireTime()));
        return demandMapper.selectById(id);
    }

    @Override
    public void offlineDemand(Long userId, Long id) {
        validateDemandOwner(userId, id);
        demandMapper.updateById(TutorDemandDO.builder()
                .id(id)
                .status(TutorPublishStatusEnum.OFFLINE.getStatus())
                .build());
    }

    @Override
    public TutorDemandDO offlineDemandByAdmin(Long id) {
        TutorDemandDO demand = demandMapper.selectById(id);
        if (demand == null) {
            throw exception(DEMAND_NOT_EXISTS);
        }
        demandMapper.updateById(TutorDemandDO.builder()
                .id(id)
                .status(TutorPublishStatusEnum.OFFLINE.getStatus())
                .build());
        return demandMapper.selectById(id);
    }

    @Override
    public List<TutorDemandDO> getMyDemandList(Long userId) {
        return demandMapper.selectListByUserId(userId);
    }

    @Override
    public PageResult<TutorDemandDO> getDemandPage(AdminTutorDemandPageReqVO reqVO) {
        return demandMapper.selectPage(reqVO);
    }

    @Override
    @Transactional
    public TutorDemandDO auditDemand(Long auditorId, AdminTutorPublishAuditReqVO reqVO) {
        TutorDemandDO demand = demandMapper.selectById(reqVO.getId());
        if (demand == null) {
            throw exception(DEMAND_NOT_EXISTS);
        }
        if (TutorAuditStatusEnum.REJECTED.getStatus().equals(reqVO.getAuditStatus())
                && StrUtil.isBlank(reqVO.getRejectReason())) {
            throw exception(PUBLISH_AUDIT_REJECT_REASON_REQUIRED);
        }
        Integer publishStatus = TutorAuditStatusEnum.APPROVED.getStatus().equals(reqVO.getAuditStatus())
                ? TutorPublishStatusEnum.SHOWING.getStatus() : TutorPublishStatusEnum.REJECTED.getStatus();
        demandMapper.update(null, new LambdaUpdateWrapper<TutorDemandDO>()
                .eq(TutorDemandDO::getId, reqVO.getId())
                .set(TutorDemandDO::getStatus, publishStatus)
                .set(TutorDemandDO::getAuditStatus, reqVO.getAuditStatus())
                .set(TutorDemandDO::getRejectReason, TutorAuditStatusEnum.REJECTED.getStatus().equals(reqVO.getAuditStatus())
                        ? reqVO.getRejectReason() : null));
        TutorDemandDO updatedDemand = demandMapper.selectById(reqVO.getId());
        tutorNotifyService.sendPublishAuditResult(updatedDemand.getUserId(), "家长需求", updatedDemand.getTitle(),
                updatedDemand.getAuditStatus(), updatedDemand.getRejectReason());
        return updatedDemand;
    }

    @Override
    public PageResult<TutorDemandDO> getSquareDemandPage(AppTutorDemandPageReqVO reqVO) {
        return demandMapper.selectSquarePage(reqVO);
    }

    @Override
    public TutorDemandDO getSquareDemand(Long id) {
        TutorDemandDO demand = demandMapper.selectById(id);
        if (demand == null) {
            throw exception(DEMAND_NOT_EXISTS);
        }
        if (!TutorPublishStatusEnum.SHOWING.getStatus().equals(demand.getStatus())
                || !TutorAuditStatusEnum.APPROVED.getStatus().equals(demand.getAuditStatus())
                || !demand.getExpireTime().isAfter(LocalDateTime.now())) {
            throw exception(PUBLISH_STATUS_NOT_VISIBLE);
        }
        return demand;
    }

    @Override
    public TutorDemandDO viewSquareDemand(Long id) {
        TutorDemandDO demand = getSquareDemand(id);
        demandMapper.updateViewCountIncr(id);
        return demandMapper.selectById(id);
    }

    private TutorDemandDO.TutorDemandDOBuilder buildDemand(Long userId, TutorCityDO city, AppTutorDemandSaveReqVO reqVO) {
        return TutorDemandDO.builder()
                .userId(userId)
                .title(reqVO.getTitle())
                .grade(reqVO.getGrade())
                .subjects(reqVO.getSubjects())
                .teachMode(reqVO.getTeachMode())
                .budgetMin(reqVO.getBudgetMin())
                .budgetMax(reqVO.getBudgetMax())
                .description(reqVO.getDescription())
                .cityCode(city.getCode())
                .cityName(city.getName())
                .longitude(reqVO.getLongitude())
                .latitude(reqVO.getLatitude())
                .distanceVisible(!Boolean.FALSE.equals(reqVO.getDistanceVisible()))
                .contactMobileEncrypt(reqVO.getContactMobile())
                .contactMobileMask(DesensitizedUtil.mobilePhone(reqVO.getContactMobile()))
                .contactWechatEncrypt(reqVO.getContactWechat())
                .contactWechatMask(maskWechat(reqVO.getContactWechat()));
    }

    private void saveParentProfileFromDemand(Long userId, AppTutorDemandSaveReqVO reqVO) {
        AppTutorParentProfileSaveReqVO parentReqVO = new AppTutorParentProfileSaveReqVO();
        parentReqVO.setChildGrade(reqVO.getGrade());
        parentReqVO.setSubjects(reqVO.getSubjects());
        parentReqVO.setBudgetMin(reqVO.getBudgetMin());
        parentReqVO.setBudgetMax(reqVO.getBudgetMax());
        parentReqVO.setTeachMode(reqVO.getTeachMode());
        parentReqVO.setRemark(reqVO.getDescription());
        parentProfileService.saveParentProfile(userId, parentReqVO);
    }

    private TutorDemandDO validateDemandOwner(Long userId, Long id) {
        TutorDemandDO demand = demandMapper.selectById(id);
        if (demand == null) {
            throw exception(DEMAND_NOT_EXISTS);
        }
        if (!Objects.equals(userId, demand.getUserId())) {
            throw exception(PUBLISH_OPERATION_FORBIDDEN);
        }
        return demand;
    }

    private void validateActivePublishCount(Long userId) {
        Long count = demandMapper.selectCountByUserIdAndStatuses(userId, ACTIVE_STATUSES);
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
