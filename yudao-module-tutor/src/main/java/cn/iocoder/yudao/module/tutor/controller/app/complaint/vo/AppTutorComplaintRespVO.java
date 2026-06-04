package cn.iocoder.yudao.module.tutor.controller.app.complaint.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppTutorComplaintRespVO {
    private Long id;
    private String targetType;
    private Long targetId;
    private String reasonType;
    private String content;
    private String imageUrls;
    private Integer status;
    private String handleResult;
    private LocalDateTime createTime;
}
