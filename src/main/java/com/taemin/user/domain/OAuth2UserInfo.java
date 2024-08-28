package com.taemin.user.domain;

import com.taemin.user.exception.AuthException;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import lombok.Builder;
import java.util.Map;
import static com.taemin.user.common.ErrorCode.ILLEGAL_REGISTRATION_ID;

@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String profile
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            case "naver" -> ofNaver(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .build();
    }

    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("nickname"))
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("profile_image"))
                .build();
    }

    public User toEntity(String registrationId) {
        return User.builder()
                .name(name)
                .email(email)
                .profile(profile)
                .role(Role.USER)
                .oauthProvider(OAuthProvider.valueOf(registrationId.toUpperCase()))
                .build();
    }
}