package cn.iocoder.yudao.module.tutor.controller.app.contact;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tutor.controller.app.common.vo.AppTutorTargetReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.contact.vo.AppTutorContactRecordRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.contact.vo.AppTutorContactRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.contact.TutorContactViewRecordDO;
import cn.iocoder.yudao.module.tutor.service.contact.TutorContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 联系方式")
@RestController
@RequestMapping("/tutor/contact")
@Validated
public class AppTutorContactController {

    @Resource
    private TutorContactService contactService;

    @PostMapping("/view")
    @Operation(summary = "扣积分并查看联系方式")
    public CommonResult<AppTutorContactRespVO> viewContact(@RequestBody @Valid AppTutorTargetReqVO reqVO) {
        return success(contactService.viewContact(getLoginUserId(), reqVO));
    }

    @GetMapping("/records")
    @Operation(summary = "获得我的联系记录")
    public CommonResult<List<AppTutorContactRecordRespVO>> getMyContactRecordList() {
        return success(contactService.getMyContactRecordList(getLoginUserId()).stream()
                .map(AppTutorContactController::convert).collect(Collectors.toList()));
    }

    private static AppTutorContactRecordRespVO convert(TutorContactViewRecordDO record) {
        return AppTutorContactRecordRespVO.builder()
                .id(record.getId()).targetType(record.getTargetType()).targetId(record.getTargetId())
                .targetUserId(record.getTargetUserId()).pointCost(record.getPointCost())
                .freeReuseUntil(record.getFreeReuseUntil()).createTime(record.getCreateTime()).build();
    }

}
