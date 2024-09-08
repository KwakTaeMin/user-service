package com.taemin.user.domain.user;

import com.taemin.user.exception.AuthException;
import com.taemin.user.external.dto.response.KakaoUserResponse;
import com.taemin.user.external.dto.response.GoogleUserResponse;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.Map;

import static com.taemin.user.common.ErrorCode.ILLEGAL_REGISTRATION_ID;

@Builder(access = AccessLevel.PRIVATE)
public record OAuth2User(
        String oAuthId,
        String name,
        String email,
        String profile
) {
    public static OAuth2User of(GoogleUserResponse googleUser) {
        return new OAuth2User(googleUser.getSub(), googleUser.getName(), googleUser.getEmail(), googleUser.getPicture());
    }

    public static OAuth2User of(KakaoUserResponse kakaoUserResponse) {
        return new OAuth2User(kakaoUserResponse.getSub(), kakaoUserResponse.getName(), kakaoUserResponse.getEmail(), kakaoUserResponse.getPicture());
    }

    public static OAuth2User of(OAuthProvider oAuthProvider, Map<String, Object> attributes) {
        return switch (oAuthProvider) { // registration id별로 userInfo 생성
            case GOOGLE -> ofGoogle(attributes);
            case KAKAO -> ofKakao(attributes);
            case NAVER -> ofNaver(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2User ofGoogle(Map<String, Object> attributes) {
        return OAuth2User.builder()
                .oAuthId((String) attributes.get("sub"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .build();
    }

    private static OAuth2User ofKakao(Map<String, Object> attributes) {
        @SuppressWarnings("unchecked")
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        @SuppressWarnings("unchecked")
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2User.builder()
                .oAuthId(String.valueOf(attributes.get("id")))
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .build();
    }

    private static OAuth2User ofNaver(Map<String, Object> attributes) {
        @SuppressWarnings("unchecked")
        Map<String, Object> account = (Map<String, Object>) attributes.get("response");

        return OAuth2User.builder()
                .oAuthId((String) account.get("id"))
                .name((String) account.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) account.get("profile_image"))
                .build();
    }

    public User toEntity(OAuthProvider oAuthProvider) {
        return User.of(
                Name.of(name),
                Email.of(email),
                Profile.of(profile),
                Role.USER,
                oAuthProvider,
                OAuthId.of(oAuthId)
        );

    }
}