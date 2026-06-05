package cn.iocoder.yudao.module.tutor.service.value;

import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.tutor.controller.app.value.vo.AppTutorValueServiceBuyReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.value.TutorValueServiceConfigDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.value.TutorValueServiceOrderDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.value.TutorValueServiceConfigMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.value.TutorValueServiceOrderMapper;
import cn.iocoder.yudao.module.tutor.enums.target.TutorTargetTypeEnum;
import cn.iocoder.yudao.module.tutor.enums.value.TutorValueServiceTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.*;

@Service
@Validated
public class TutorValueServiceServiceImpl implements TutorValueServiceService {

    @Resource
    private TutorValueServiceConfigMapper tutorValueServiceConfigMapper;
    @Resource
    private TutorValueServiceOrderMapper orderMapper;
    @Resource
    private TutorDemandMapper demandMapper;
    @Resource
    private TutorTeacherResumeMapper resumeMapper;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private MemberPointApi memberPointApi;

    @Override
    public List<TutorValueServiceConfigDO> getEnabledConfigList(String targetType) {
        return tutorValueServiceConfigMapper.selectEnabledList(targetType);
    }

    @Override
    @Transactional
    public TutorValueServiceOrderDO buyService(Long userId, AppTutorValueServiceBuyReqVO reqVO) {
        TutorValueServiceConfigDO config = tutorValueServiceConfigMapper.selectById(reqVO.getConfigId());
        if (config == null || config.getStatus() == null || config.getStatus() != 0) {
            throw exception(VALUE_SERVICE_CONFIG_NOT_EXISTS);
        }
        if (!Objects.equals(config.getTargetType(), reqVO.getTargetType())) {
            throw exception(VALUE_SERVICE_TARGET_TYPE_NOT_SUPPORT);
        }
        validateOwner(userId, reqVO.getTargetType(), reqVO.getTargetId());
        MemberUserRespDTO user = memberUserApi.getUser(userId);
        if (user == null || user.getPoint() == null || user.getPoint() < config.getPointPrice()) {
            throw exception(VALUE_SERVICE_POINT_NOT_ENOUGH);
        }
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(config.getDurationHours());
        TutorValueServiceOrderDO order = TutorValueServiceOrderDO.builder()
                .userId(userId)
                .configId(config.getId())
                .serviceType(config.getServiceType())
                .targetType(reqVO.getTargetType())
                .targetId(reqVO.getTargetId())
                .pointPrice(config.getPointPrice())
                .effectStartTime(start)
                .effectEndTime(end)
                .status(1)
                .build();
        orderMapper.insert(order);
        String bizId = "value-service:" + reqVO.getTargetType() + ":" + reqVO.getTargetId() + ":" + config.getServiceType();
        memberPointApi.reducePoint(userId, config.getPointPrice(),
                MemberPointBizTypeEnum.TUTOR_VALUE_SERVICE.getType(), bizId);
        applyEffect(reqVO.getTargetType(), reqVO.getTargetId(), config.getServiceType(), end);
        return order;
    }

    private void validateOwner(Long userId, String targetType, Long targetId) {
        if (TutorTargetTypeEnum.isDemand(targetType)) {
            TutorDemandDO demand = demandMapper.selectById(targetId);
            if (demand == null || !Objects.equals(demand.getUserId(), userId)) {
                throw exception(VALUE_SERVICE_TARGET_NOT_EXISTS);
            }
            return;
        }
        if (TutorTargetTypeEnum.isResume(targetType)) {
            TutorTeacherResumeDO resume = resumeMapper.selectById(targetId);
            if (resume == null || !Objects.equals(resume.getUserId(), userId)) {
                throw exception(VALUE_SERVICE_TARGET_NOT_EXISTS);
            }
            return;
        }
        throw exception(VALUE_SERVICE_TARGET_TYPE_NOT_SUPPORT);
    }

    private void applyEffect(String targetType, Long targetId, String serviceType, LocalDateTime effectEndTime) {
        if (TutorTargetTypeEnum.isDemand(targetType)) {
            TutorDemandDO update = TutorDemandDO.builder().id(targetId).build();
            fillEffect(update, serviceType, effectEndTime);
            demandMapper.updateById(update);
            return;
        }
        TutorTeacherResumeDO update = TutorTeacherResumeDO.builder().id(targetId).build();
        fillEffect(update, serviceType, effectEndTime);
        resumeMapper.updateById(update);
    }

    private void fillEffect(TutorDemandDO update, String serviceType, LocalDateTime effectEndTime) {
        if (TutorValueServiceTypeEnum.TOP.getType().equals(serviceType)) {
            update.setTopUntil(effectEndTime);
        } else if (TutorValueServiceTypeEnum.URGENT.getType().equals(serviceType)) {
            update.setUrgentUntil(effectEndTime);
        } else if (TutorValueServiceTypeEnum.BOOST.getType().equals(serviceType)) {
            update.setBoostUntil(effectEndTime);
        }
    }

    private void fillEffect(TutorTeacherResumeDO update, String serviceType, LocalDateTime effectEndTime) {
        if (TutorValueServiceTypeEnum.TOP.getType().equals(serviceType)) {
            update.setTopUntil(effectEndTime);
        } else if (TutorValueServiceTypeEnum.URGENT.getType().equals(serviceType)) {
            update.setUrgentUntil(effectEndTime);
        } else if (TutorValueServiceTypeEnum.BOOST.getType().equals(serviceType)) {
            update.setBoostUntil(effectEndTime);
        }
    }
}
