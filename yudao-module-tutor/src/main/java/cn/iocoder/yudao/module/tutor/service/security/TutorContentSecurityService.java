package cn.iocoder.yudao.module.tutor.service.security;

import java.util.Collection;

public interface TutorContentSecurityService {

    void validateText(String scene, String text);

    void validateTexts(String scene, Collection<String> texts);

    void validateImageUrl(String scene, String imageUrl);
}
