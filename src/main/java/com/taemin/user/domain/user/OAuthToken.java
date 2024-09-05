package com.taemin.user.domain.user;

import com.taemin.user.type.OAuthProvider;
import lombok.Getter;

@Getter
public class OAuthToken {
    private final String accessToken;
    private final String refreshToken;
    private final OAuthProvider oAuthProvider;

    private OAuthToken(String accessToken, String refreshToken, OAuthProvider oAuthProvider) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.oAuthProvider = oAuthProvider;
    }

    public static OAuthToken of(String accessToken, String refreshToken, OAuthProvider oAuthProvider) {
        return new OAuthToken(accessToken, refreshToken, oAuthProvider);
    }
}
