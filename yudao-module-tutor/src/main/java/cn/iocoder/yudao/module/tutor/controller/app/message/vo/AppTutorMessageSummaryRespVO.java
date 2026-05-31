package cn.iocoder.yudao.module.tutor.controller.app.message.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppTutorMessageSummaryRespVO {

    private Long totalUnread;
    private List<CategorySummary> categories;

    @Data
    public static class CategorySummary {

        private String category;
        private String latestContent;
        private LocalDateTime latestTime;
        private Long unreadCount;

    }

}
