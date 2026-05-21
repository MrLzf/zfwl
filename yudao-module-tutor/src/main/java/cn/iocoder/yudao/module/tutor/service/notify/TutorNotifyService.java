package cn.iocoder.yudao.module.tutor.service.notify;

/**
 * 家教站内信通知 Service。
 */
public interface TutorNotifyService {

    void sendCertificationAuditResult(Long userId, Integer status, String rejectReason);

    void sendPublishAuditResult(Long userId, String publishType, String title, Integer auditStatus, String rejectReason);

    void sendPointChanged(Long userId, String scene, Integer point);

    void sendMatchSuccess(Long parentUserId, Long teacherUserId, Long matchId);

    void sendReviewCreated(Long targetUserId, Long reviewerUserId, Integer rating);

}
