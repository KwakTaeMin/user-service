package com.taemin.user.domain.user;

import com.taemin.user.type.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OAuthTokenTest {

    @Test
    @DisplayName("OAuthToken 생성시 정상적으로 생성되는지 확인")
    void createOAuthToken() {
        // given
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        OAuthProvider oAuthProvider = OAuthProvider.GOOGLE;

        // when
        OAuthToken oAuthToken = OAuthToken.of(accessToken, refreshToken, oAuthProvider);

        // then
        assertThat(oAuthToken.getAccessToken()).isEqualTo(accessToken);
        assertThat(oAuthToken.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(oAuthToken.getOAuthProvider()).isEqualTo(oAuthProvider);
    }
}