package com.taemin.user.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.taemin.user.domain.user.Email;
import com.taemin.user.domain.user.Name;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.Profile;
import com.taemin.user.domain.user.User;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import java.time.LocalDateTime;

public record UserResponse(
    Long userId,
    @JsonUnwrapped Name name,
    @JsonUnwrapped Email email,
    @JsonUnwrapped Profile profile,
    Role role,
    @JsonUnwrapped OAuthId oauthId,
    OAuthProvider oauthProvider,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponse of(User user) {
        return new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getProfile(), user.getRole(), user.getOauthId(),
                                user.getOauthProvider(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
