package com.taemin.user.external.dto.response;

import com.taemin.user.type.OAuthProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleUserResponse implements OAuthUserResponse {
    private String sub;
    private String name;
    private String picture;
    private String email;

    public GoogleUserResponse(String sub, String name, String picture, String email) {
        this.sub = sub;
        this.name = name;
        this.picture = picture;
        this.email = email;
    }

    @Override
    public String getId() {
        return sub;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProfileImage() {
        return picture;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.GOOGLE;
    }
}
