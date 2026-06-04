package cn.iocoder.yudao.module.tutor.service.security;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tutor.enums.ErrorCodeConstants.CONTENT_SECURITY_REJECTED;

@Service
public class TutorContentSecurityServiceImpl implements TutorContentSecurityService {

    private static final List<String> SENSITIVE_WORDS = Arrays.asList("赌博", "诈骗", "代考", "办证", "裸聊");

    @Override
    public void validateText(String scene, String text) {
        if (StrUtil.isBlank(text)) {
            return;
        }
        String normalized = text.toLowerCase();
        for (String word : SENSITIVE_WORDS) {
            if (normalized.contains(word.toLowerCase())) {
                throw exception(CONTENT_SECURITY_REJECTED);
            }
        }
    }

    @Override
    public void validateTexts(String scene, Collection<String> texts) {
        if (texts == null) {
            return;
        }
        for (String text : texts) {
            validateText(scene, text);
        }
    }

    @Override
    public void validateImageUrl(String scene, String imageUrl) {
        if (StrUtil.isBlank(imageUrl)) {
            return;
        }
        validateText(scene, imageUrl);
    }
}
