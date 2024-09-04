package com.taemin.user.domain.token;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AccessToken {

    private String accessToken;

    private AccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static AccessToken of(String accessToken) {
        return new AccessToken(accessToken);
    }
}
