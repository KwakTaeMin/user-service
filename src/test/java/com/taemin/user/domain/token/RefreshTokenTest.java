package com.taemin.user.domain.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenTest {

    @Test
    @DisplayName("RefreshToken 생성시 정상적으로 생성되는지 확인")
    void createRefreshToken() {
        // given
        String refreshTokenValue = "test-refresh-token";

        // when
        RefreshToken refreshToken = RefreshToken.of(refreshTokenValue);

        // then
        assertThat(refreshToken.getRefreshToken()).isEqualTo(refreshTokenValue);
    }
}