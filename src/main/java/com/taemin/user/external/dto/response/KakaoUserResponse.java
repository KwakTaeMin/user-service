package com.taemin.user.external.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KakaoUserResponse {
    private Long id;
    private String connectedAt;
    private Properties properties;
    private KakaoAccount kakaoAccount;

    @Getter
    @AllArgsConstructor
    public static class Properties {
        private String nickname;
        private String profileImage;
        private String thumbnailImage;
    }

    @Getter
    @AllArgsConstructor
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @AllArgsConstructor
        public static class Profile {
            private String nickname;
            private String profileImageUrl;
        }
    }

    public String getNickName() {
        return properties.getNickname();
    }

    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    public String getProfileImageUrl() {
        return kakaoAccount.getProfile().getProfileImageUrl();
    }
}
