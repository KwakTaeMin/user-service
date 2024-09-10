package com.taemin.user.external.dto.response;


import com.taemin.user.type.OAuthProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NaverUserResponse implements OAuthUserResponse{
    private String resultcode;
    private String message;
    private Response response;

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String email;
        private String name;
        private String profileImage;
    }

    @Override
    public String getId() {
        return response.getId();
    }

    @Override
    public String getEmail() {
        return response.getEmail();
    }

    @Override
    public String getName() {
        return response.getName();
    }

    @Override
    public String getProfileImage() {
        return response.getProfileImage();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }
}
