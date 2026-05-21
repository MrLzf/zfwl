package cn.iocoder.yudao.module.tutor.controller.app.detail.vo;

import cn.iocoder.yudao.module.tutor.controller.app.demand.vo.AppTutorDemandRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.contact.vo.AppTutorContactRespVO;
import cn.iocoder.yudao.module.tutor.controller.app.resume.vo.AppTutorTeacherResumeRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "用户 App - 家教详情 Response VO")
@Data
@Builder
public class AppTutorDetailRespVO {

    @Schema(description = "目标类型：demand 家长需求，resume 教师简历")
    private String targetType;

    @Schema(description = "家长需求详情")
    private AppTutorDemandRespVO demand;

    @Schema(description = "教师简历详情")
    private AppTutorTeacherResumeRespVO resume;

    @Schema(description = "联系方式是否已解锁")
    private Boolean contactUnlocked;

    @Schema(description = "已解锁联系方式，未解锁或未登录为空")
    private AppTutorContactRespVO contact;

    @Schema(description = "安全提示")
    private String safetyTip;

}
