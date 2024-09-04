package com.taemin.user.domain.token;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RefreshToken {
    private String refreshToken;

    private RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static RefreshToken of(String refreshToken) {
        return new RefreshToken(refreshToken);
    }
}
