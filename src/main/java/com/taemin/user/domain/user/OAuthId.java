package com.taemin.user.domain.user;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.exception.UserException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class OAuthId {

    @Column(nullable = false)
    private String oauthId;

    private OAuthId(String oauthId) {
        validateOAuthId(oauthId);
        this.oauthId = oauthId;
    }

    public static OAuthId of(String oauthId) {
        return new OAuthId(oauthId);
    }

    private static void validateOAuthId(String oauthId) {
        if (oauthId == null || oauthId.trim().isEmpty()) {
            throw new UserException(ErrorCode.USER_OAUTH_ID_VALIDATE_FAIL);
        }
    }
}
