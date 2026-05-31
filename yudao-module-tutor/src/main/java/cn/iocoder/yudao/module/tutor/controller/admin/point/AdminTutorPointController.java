package cn.iocoder.yudao.module.tutor.controller.admin.point;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.member.api.point.MemberPointApi;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.tutor.controller.admin.point.vo.AdminTutorPointAdjustReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.point.TutorPointAdjustRecordDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.point.TutorPointAdjustRecordMapper;
import cn.iocoder.yudao.module.tutor.service.notify.TutorNotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 家教积分")
@RestController
@RequestMapping("/tutor/points")
@Validated
public class AdminTutorPointController {

    @Resource
    private MemberPointApi memberPointApi;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private TutorPointAdjustRecordMapper pointAdjustRecordMapper;
    @Resource
    private TutorNotifyService tutorNotifyService;

    @PutMapping("/adjust")
    @Operation(summary = "后台调整家教用户积分")
    @PreAuthorize("@ss.hasPermission('tutor:point:adjust')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> adjustPoint(@RequestBody @Valid AdminTutorPointAdjustReqVO reqVO) {
        memberUserApi.validateUser(reqVO.getUserId());
        Long operatorId = getLoginUserId();
        TutorPointAdjustRecordDO record = TutorPointAdjustRecordDO.builder()
                .userId(reqVO.getUserId())
                .point(reqVO.getPoint())
                .remark(reqVO.getRemark())
                .operatorId(operatorId)
                .build();
        pointAdjustRecordMapper.insert(record);

        String bizId = "tutor:" + operatorId + ":" + record.getId();
        if (reqVO.getPoint() > 0) {
            memberPointApi.addPoint(reqVO.getUserId(), reqVO.getPoint(),
                    MemberPointBizTypeEnum.ADMIN.getType(), bizId);
        } else {
            memberPointApi.reducePoint(reqVO.getUserId(), -reqVO.getPoint(),
                    MemberPointBizTypeEnum.ADMIN.getType(), bizId);
        }
        MemberUserRespDTO user = memberUserApi.getUser(reqVO.getUserId());
        tutorNotifyService.sendPointChanged(reqVO.getUserId(), "后台积分调整", reqVO.getPoint(),
                user == null ? null : user.getPoint(), "point", "point_records", bizId, null, null);
        return success(true);
    }

}
