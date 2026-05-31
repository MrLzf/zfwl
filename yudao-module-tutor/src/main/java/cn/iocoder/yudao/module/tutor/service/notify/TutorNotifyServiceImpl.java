package cn.iocoder.yudao.module.tutor.service.notify;

import cn.iocoder.yudao.module.system.api.notify.NotifyMessageSendApi;
import cn.iocoder.yudao.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import cn.iocoder.yudao.module.tutor.enums.audit.TutorAuditStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 家教站内信通知 Service 实现。
 */
@Service
@Slf4j
public class TutorNotifyServiceImpl implements TutorNotifyService {

    private static final String TEMPLATE_CERTIFICATION_AUDIT = "tutor_certification_audit";
    private static final String TEMPLATE_PUBLISH_AUDIT = "tutor_publish_audit";
    private static final String TEMPLATE_POINT_CHANGED = "tutor_point_changed";
    private static final String TEMPLATE_CONTACT_VIEWER = "tutor_contact_viewer";
    private static final String TEMPLATE_CONTACT_OWNER = "tutor_contact_owner";
    private static final String TEMPLATE_MATCH_SUCCESS = "tutor_match_success";
    private static final String TEMPLATE_REVIEW_CREATED = "tutor_review_created";

    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    @Override
    public void sendCertificationAuditResult(Long userId, Integer status, String rejectReason) {
        sendCertificationAuditResult(userId, status, rejectReason, "", "", null);
    }

    @Override
    public void sendCertificationAuditResult(Long userId, Integer status, String rejectReason,
                                             String bizId, String targetType, Long targetId) {
        Map<String, Object> params = buildCommonParams("audit", "certification_detail", bizId, targetType, targetId);
        params.put("status", getAuditStatusName(status));
        params.put("reason", rejectReason == null ? "" : rejectReason);
        send(userId, TEMPLATE_CERTIFICATION_AUDIT, params);
    }

    @Override
    public void sendPublishAuditResult(Long userId, String publishType, String title, Integer auditStatus, String rejectReason) {
        sendPublishAuditResult(userId, publishType, title, auditStatus, rejectReason, "", "", null);
    }

    @Override
    public void sendPublishAuditResult(Long userId, String publishType, String title, Integer auditStatus, String rejectReason,
                                       String bizId, String targetType, Long targetId) {
        Map<String, Object> params = buildCommonParams("audit", "my_posts", bizId, targetType, targetId);
        params.put("publishType", publishType);
        params.put("title", title);
        params.put("status", getAuditStatusName(auditStatus));
        params.put("reason", rejectReason == null ? "" : rejectReason);
        send(userId, TEMPLATE_PUBLISH_AUDIT, params);
    }

    @Override
    public void sendPointChanged(Long userId, String scene, Integer point) {
        Map<String, Object> params = buildCommonParams("", "", "", "", null);
        params.put("title", scene);
        params.put("scene", scene);
        params.put("point", point);
        params.put("totalPoint", "");
        send(userId, TEMPLATE_POINT_CHANGED, params);
    }

    @Override
    public void sendPointChanged(Long userId, String title, Integer point, Integer totalPoint,
                                 String category, String action, String bizId, String targetType, Long targetId) {
        Map<String, Object> params = buildCommonParams(category, action, bizId, targetType, targetId);
        params.put("title", title);
        params.put("scene", title);
        params.put("point", point);
        params.put("totalPoint", totalPoint);
        send(userId, TEMPLATE_POINT_CHANGED, params);
    }

    @Override
    public void sendContactViewer(Long viewerUserId, Long ownerUserId, String targetType, Long targetId) {
        sendContactViewer(viewerUserId, ownerUserId, "", "", false, targetType, targetId);
    }

    @Override
    public void sendContactOwner(Long ownerUserId, Long viewerUserId, String targetType, Long targetId) {
        sendContactOwner(ownerUserId, viewerUserId, "", "", false, targetType, targetId);
    }

    @Override
    public void sendContactViewed(Long viewerUserId, Long ownerUserId, String targetType, Long targetId) {
        sendContactViewed(viewerUserId, ownerUserId, "", "", false, targetType, targetId);
    }

    @Override
    public void sendContactViewed(Long viewerUserId, Long ownerUserId, String counterpartName, String contentTitle,
                                  Boolean reuse, String targetType, Long targetId) {
        sendContactViewer(viewerUserId, ownerUserId, counterpartName, contentTitle, reuse, targetType, targetId);
        sendContactOwner(ownerUserId, viewerUserId, counterpartName, contentTitle, reuse, targetType, targetId);
    }

    @Override
    public void sendContactViewed(Long viewerUserId, Long ownerUserId, String viewerName, String ownerName,
                                  String contentTitle, Boolean reuse, String targetType, Long targetId) {
        sendContactViewer(viewerUserId, ownerUserId, ownerName, contentTitle, reuse, targetType, targetId);
        sendContactOwner(ownerUserId, viewerUserId, viewerName, contentTitle, reuse, targetType, targetId);
    }

    @Override
    public void sendContactViewer(Long viewerUserId, Long ownerUserId, String counterpartName, String contentTitle,
                                  Boolean reuse, String targetType, Long targetId) {
        Map<String, Object> params = buildContactParams("contact_records", ownerUserId, counterpartName, contentTitle, reuse,
                targetType, targetId);
        send(viewerUserId, TEMPLATE_CONTACT_VIEWER, params);
    }

    @Override
    public void sendContactOwner(Long ownerUserId, Long viewerUserId, String counterpartName, String contentTitle,
                                 Boolean reuse, String targetType, Long targetId) {
        Map<String, Object> params = buildContactParams("contact_records", viewerUserId, counterpartName, contentTitle, reuse,
                targetType, targetId);
        send(ownerUserId, TEMPLATE_CONTACT_OWNER, params);
    }

    @Override
    public void sendMatchSuccess(Long parentUserId, Long teacherUserId, Long matchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);
        send(parentUserId, TEMPLATE_MATCH_SUCCESS, params);
        send(teacherUserId, TEMPLATE_MATCH_SUCCESS, params);
    }

    @Override
    public void sendReviewCreated(Long targetUserId, Long reviewerUserId, Integer rating) {
        Map<String, Object> params = new HashMap<>();
        params.put("reviewerUserId", reviewerUserId);
        params.put("rating", rating);
        send(targetUserId, TEMPLATE_REVIEW_CREATED, params);
    }

    private void send(Long userId, String templateCode, Map<String, Object> params) {
        try {
            NotifySendSingleToUserReqDTO reqDTO = new NotifySendSingleToUserReqDTO();
            reqDTO.setUserId(userId);
            reqDTO.setTemplateCode(templateCode);
            reqDTO.setTemplateParams(params);
            notifyMessageSendApi.sendSingleMessageToMember(reqDTO);
        } catch (Exception ex) {
            log.warn("[send][userId({}) templateCode({}) params({}) 家教站内信发送失败]", userId, templateCode, params, ex);
        }
    }

    private Map<String, Object> buildCommonParams(String category, String action, String bizId,
                                                   String targetType, Long targetId) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("action", action);
        params.put("bizId", bizId);
        params.put("targetType", targetType);
        params.put("targetId", targetId);
        return params;
    }

    private Map<String, Object> buildContactParams(String action, Long counterpartUserId, String counterpartName,
                                                    String contentTitle, Boolean reuse, String targetType, Long targetId) {
        Map<String, Object> params = buildCommonParams("contact", action, targetType + ":" + targetId, targetType, targetId);
        params.put("counterpartUserId", counterpartUserId);
        params.put("counterpartName", counterpartName);
        params.put("contentTitle", contentTitle);
        params.put("reuse", reuse);
        return params;
    }

    private String getAuditStatusName(Integer status) {
        for (TutorAuditStatusEnum statusEnum : TutorAuditStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum.getName();
            }
        }
        return "未知";
    }

}
