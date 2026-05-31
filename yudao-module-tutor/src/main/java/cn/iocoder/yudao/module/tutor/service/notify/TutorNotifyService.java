package cn.iocoder.yudao.module.tutor.service.notify;

/**
 * 家教站内信通知 Service。
 */
public interface TutorNotifyService {

    void sendCertificationAuditResult(Long userId, Integer status, String rejectReason);

    void sendCertificationAuditResult(Long userId, Integer status, String rejectReason,
                                      String bizId, String targetType, Long targetId);

    void sendPublishAuditResult(Long userId, String publishType, String title, Integer auditStatus, String rejectReason);

    void sendPublishAuditResult(Long userId, String publishType, String title, Integer auditStatus, String rejectReason,
                                String bizId, String targetType, Long targetId);

    void sendPointChanged(Long userId, String scene, Integer point);

    void sendPointChanged(Long userId, String title, Integer point, Integer totalPoint,
                          String category, String action, String bizId, String targetType, Long targetId);

    void sendContactViewer(Long viewerUserId, Long ownerUserId, String targetType, Long targetId);

    void sendContactViewer(Long viewerUserId, Long ownerUserId, String counterpartName, String contentTitle,
                           Boolean reuse, String targetType, Long targetId);

    void sendContactOwner(Long ownerUserId, Long viewerUserId, String targetType, Long targetId);

    void sendContactOwner(Long ownerUserId, Long viewerUserId, String counterpartName, String contentTitle,
                          Boolean reuse, String targetType, Long targetId);

    void sendContactViewed(Long viewerUserId, Long ownerUserId, String targetType, Long targetId);

    void sendContactViewed(Long viewerUserId, Long ownerUserId, String counterpartName, String contentTitle,
                           Boolean reuse, String targetType, Long targetId);

    void sendContactViewed(Long viewerUserId, Long ownerUserId, String viewerName, String ownerName,
                           String contentTitle, Boolean reuse, String targetType, Long targetId);

    void sendMatchSuccess(Long parentUserId, Long teacherUserId, Long matchId);

    void sendReviewCreated(Long targetUserId, Long reviewerUserId, Integer rating);

}
