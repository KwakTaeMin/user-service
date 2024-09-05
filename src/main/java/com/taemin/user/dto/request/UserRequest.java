package com.taemin.user.dto.request;

import com.taemin.user.domain.user.*;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull OAuthProvider oAuthProvider,
        @NotNull String oauthId,
        @NotNull String name,
        @NotNull String email,
        @NotNull String accessToken,
        String profile,
        String refreshToken
) {
    public User toUser() {
        return User.of(Name.of(name), Email.of(email), Profile.of(profile), Role.USER, oAuthProvider, OAuthId.of(oauthId));
    }
}
