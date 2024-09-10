package com.taemin.user.domain.user;

import com.taemin.user.external.dto.response.OAuthUserResponse;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record OAuth2User(
    String oAuthId,
    String name,
    String email,
    String profile,
    OAuthProvider oAuthProvider
) {
    public static OAuth2User of(OAuthUserResponse oAuthUser) {
        return new OAuth2User(oAuthUser.getId(), oAuthUser.getName(), oAuthUser.getEmail(), oAuthUser.getProfileImage(),
                              oAuthUser.getOAuthProvider());
    }

    public User toEntity() {
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