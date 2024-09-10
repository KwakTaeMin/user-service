package com.taemin.user.external.dto.response;

import com.taemin.user.type.OAuthProvider;

public interface OAuthUserResponse {
    String getId();
    String getEmail();
    String getName();
    String getProfileImage();
    OAuthProvider getOAuthProvider();
}
