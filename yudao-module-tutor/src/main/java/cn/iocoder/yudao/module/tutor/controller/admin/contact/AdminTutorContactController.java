package cn.iocoder.yudao.module.tutor.controller.admin.contact;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.tutor.controller.admin.contact.vo.AdminTutorContactPageReqVO;
import cn.iocoder.yudao.module.tutor.controller.admin.contact.vo.AdminTutorContactRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.contact.TutorContactViewRecordDO;
import cn.iocoder.yudao.module.tutor.service.contact.TutorContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static java.util.Collections.emptyMap;

@Tag(name = "管理后台 - 家教联系记录")
@RestController
@RequestMapping("/tutor/contacts")
@Validated
public class AdminTutorContactController {

    @Resource
    private TutorContactService contactService;
    @Resource
    private MemberUserApi memberUserApi;

    @GetMapping("/page")
    @Operation(summary = "获得联系记录分页")
    @PreAuthorize("@ss.hasPermission('tutor:contact:query')")
    public CommonResult<PageResult<AdminTutorContactRespVO>> getContactPage(@Valid AdminTutorContactPageReqVO pageReqVO) {
        PageResult<TutorContactViewRecordDO> pageResult = contactService.getContactPage(pageReqVO);
        Set<Long> userIds = new HashSet<>();
        pageResult.getList().forEach(record -> {
            userIds.add(record.getViewerUserId());
            userIds.add(record.getTargetUserId());
        });
        Map<Long, MemberUserRespDTO> userMap = userIds.isEmpty() ? emptyMap() : memberUserApi.getUserMap(userIds);
        return success(new PageResult<>(pageResult.getList().stream()
                .map(record -> convert(record, userMap))
                .collect(Collectors.toList()), pageResult.getTotal()));
    }

    private AdminTutorContactRespVO convert(TutorContactViewRecordDO record, Map<Long, MemberUserRespDTO> userMap) {
        MemberUserRespDTO viewer = userMap.get(record.getViewerUserId());
        MemberUserRespDTO target = userMap.get(record.getTargetUserId());
        return AdminTutorContactRespVO.builder()
                .id(record.getId())
                .viewerUserId(record.getViewerUserId())
                .viewerNickname(viewer == null ? null : viewer.getNickname())
                .viewerMobile(viewer == null ? null : viewer.getMobile())
                .targetType(record.getTargetType())
                .targetId(record.getTargetId())
                .targetUserId(record.getTargetUserId())
                .targetNickname(target == null ? null : target.getNickname())
                .targetMobile(target == null ? null : target.getMobile())
                .pointCost(record.getPointCost())
                .freeReuseUntil(record.getFreeReuseUntil())
                .viewedMobile(record.getViewedMobile())
                .viewedWechat(record.getViewedWechat())
                .createTime(record.getCreateTime())
                .build();
    }

}
