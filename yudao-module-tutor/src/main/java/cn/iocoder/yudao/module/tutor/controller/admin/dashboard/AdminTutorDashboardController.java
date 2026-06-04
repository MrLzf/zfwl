package cn.iocoder.yudao.module.tutor.controller.admin.dashboard;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.tutor.controller.admin.dashboard.vo.AdminTutorDashboardSummaryRespVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.certification.TutorCertificationDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.demand.TutorDemandDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.match.TutorMatchRecordDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.resume.TutorTeacherResumeDO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.review.TutorReviewDO;
import cn.iocoder.yudao.module.tutor.dal.mysql.certification.TutorCertificationMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.contact.TutorContactViewRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.demand.TutorDemandMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.match.TutorMatchRecordMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.profile.TutorUserProfileMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.resume.TutorTeacherResumeMapper;
import cn.iocoder.yudao.module.tutor.dal.mysql.review.TutorReviewMapper;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.match.TutorMatchStatusEnum;
import cn.iocoder.yudao.module.tutor.enums.profile.TutorUserRoleEnum;
import cn.iocoder.yudao.module.tutor.enums.publish.TutorPublishStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 家教数据概览")
@RestController
@RequestMapping("/tutor/dashboard")
@Validated
public class AdminTutorDashboardController {

    @Resource
    private TutorUserProfileMapper profileMapper;
    @Resource
    private TutorCertificationMapper certificationMapper;
    @Resource
    private TutorDemandMapper demandMapper;
    @Resource
    private TutorTeacherResumeMapper resumeMapper;
    @Resource
    private TutorContactViewRecordMapper contactMapper;
    @Resource
    private TutorMatchRecordMapper matchMapper;
    @Resource
    private TutorReviewMapper reviewMapper;

    @GetMapping("/summary")
    @Operation(summary = "获得家教数据概览")
    @PreAuthorize("@ss.hasPermission('tutor:dashboard:query')")
    public CommonResult<AdminTutorDashboardSummaryRespVO> getSummary() {
        return success(AdminTutorDashboardSummaryRespVO.builder()
                .userCount(profileMapper.selectCount())
                .parentCount(profileMapper.selectCount(new LambdaQueryWrapperX<TutorUserProfileDO>()
                        .eq(TutorUserProfileDO::getRole, TutorUserRoleEnum.PARENT.getRole())))
                .teacherCount(profileMapper.selectCount(new LambdaQueryWrapperX<TutorUserProfileDO>()
                        .eq(TutorUserProfileDO::getRole, TutorUserRoleEnum.TEACHER.getRole())))
                .demandCount(demandMapper.selectCount())
                .resumeCount(resumeMapper.selectCount())
                .showingDemandCount(demandMapper.selectCount(new LambdaQueryWrapperX<TutorDemandDO>()
                        .eq(TutorDemandDO::getStatus, TutorPublishStatusEnum.SHOWING.getStatus())))
                .showingResumeCount(resumeMapper.selectCount(new LambdaQueryWrapperX<TutorTeacherResumeDO>()
                        .eq(TutorTeacherResumeDO::getStatus, TutorPublishStatusEnum.SHOWING.getStatus())))
                .certificationPendingCount(certificationMapper.selectCount(new LambdaQueryWrapperX<TutorCertificationDO>()
                        .eq(TutorCertificationDO::getStatus, TutorAuditStatusEnum.WAITING.getStatus())))
                .demandPendingCount(demandMapper.selectCount(new LambdaQueryWrapperX<TutorDemandDO>()
                        .eq(TutorDemandDO::getAuditStatus, TutorAuditStatusEnum.WAITING.getStatus())))
                .resumePendingCount(resumeMapper.selectCount(new LambdaQueryWrapperX<TutorTeacherResumeDO>()
                        .eq(TutorTeacherResumeDO::getAuditStatus, TutorAuditStatusEnum.WAITING.getStatus())))
                .contactViewCount(contactMapper.selectCount())
                .matchSuccessCount(matchMapper.selectCount(new LambdaQueryWrapperX<TutorMatchRecordDO>()
                        .eq(TutorMatchRecordDO::getStatus, TutorMatchStatusEnum.BOTH_CONFIRMED.getStatus())))
                .reviewCount(reviewMapper.selectCount(new LambdaQueryWrapperX<TutorReviewDO>()
                        .eq(TutorReviewDO::getStatus, 0)))
                .build());
    }

    @GetMapping("/trend")
    @Operation(summary = "获得 7/30 天运营趋势")
    @PreAuthorize("@ss.hasPermission('tutor:dashboard:query')")
    public CommonResult<List<Map<String, Object>>> getTrend(Integer days) {
        int range = days == null || days <= 0 ? 7 : days;
        Map<String, Object> today = new LinkedHashMap<>();
        today.put("date", "today");
        today.put("users", profileMapper.selectCount());
        today.put("publishes", demandMapper.selectCount() + resumeMapper.selectCount());
        today.put("contacts", contactMapper.selectCount());
        today.put("matches", matchMapper.selectCount(new LambdaQueryWrapperX<TutorMatchRecordDO>()
                .eq(TutorMatchRecordDO::getStatus, TutorMatchStatusEnum.BOTH_CONFIRMED.getStatus())));
        today.put("days", range);
        return success(Arrays.asList(today));
    }

    @GetMapping("/funnel")
    @Operation(summary = "获得运营转化漏斗")
    @PreAuthorize("@ss.hasPermission('tutor:dashboard:query')")
    public CommonResult<List<Map<String, Object>>> getFunnel() {
        Map<String, Object> registered = new LinkedHashMap<>();
        registered.put("name", "注册档案");
        registered.put("value", profileMapper.selectCount());
        Map<String, Object> published = new LinkedHashMap<>();
        published.put("name", "发布内容");
        published.put("value", demandMapper.selectCount() + resumeMapper.selectCount());
        Map<String, Object> contacted = new LinkedHashMap<>();
        contacted.put("name", "查看联系方式");
        contacted.put("value", contactMapper.selectCount());
        Map<String, Object> matched = new LinkedHashMap<>();
        matched.put("name", "匹配成功");
        matched.put("value", matchMapper.selectCount(new LambdaQueryWrapperX<TutorMatchRecordDO>()
                .eq(TutorMatchRecordDO::getStatus, TutorMatchStatusEnum.BOTH_CONFIRMED.getStatus())));
        return success(Arrays.asList(registered, published, contacted, matched));
    }

}
