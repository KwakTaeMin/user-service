package com.taemin.user.dto.request;

import com.taemin.user.domain.user.OAuthToken;
import com.taemin.user.type.OAuthProvider;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull OAuthProvider oAuthProvider,
        @NotNull String accessToken,
        String refreshToken
) {
    public OAuthToken toOauthToken() {
        return OAuthToken.of(accessToken, refreshToken, oAuthProvider);
    }
}
