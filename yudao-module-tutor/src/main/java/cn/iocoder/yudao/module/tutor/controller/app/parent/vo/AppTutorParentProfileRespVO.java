package cn.iocoder.yudao.module.tutor.controller.app.parent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "用户 App - 家长资料 Response VO")
@Data
@Builder
public class AppTutorParentProfileRespVO {

    @Schema(description = "家长资料编号", example = "1")
    private Long id;

    @Schema(description = "用户编号", example = "1024")
    private Long userId;

    @Schema(description = "家教用户档案编号", example = "10")
    private Long profileId;

    @Schema(description = "孩子年级", example = "高三")
    private String childGrade;

    @Schema(description = "辅导科目，多个用逗号分隔", example = "数学,物理")
    private String subjects;

    @Schema(description = "每小时最低预算", example = "120")
    private Integer budgetMin;

    @Schema(description = "每小时最高预算", example = "200")
    private Integer budgetMax;

    @Schema(description = "授课模式：1 上门，2 在线，3 均可", example = "3")
    private Integer teachMode;

    @Schema(description = "授课模式名称", example = "均可")
    private String teachModeName;

    @Schema(description = "补充说明", example = "希望老师周末下午上课")
    private String remark;

}
