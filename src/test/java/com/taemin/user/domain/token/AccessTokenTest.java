package com.taemin.user.domain.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccessTokenTest {

    @Test
    @DisplayName("AccessToken 생성시 정상적으로 생성되는지 확인")
    void createAccessToken() {
        // given
        String accessTokenValue = "test-access-token";

        // when
        AccessToken accessToken = AccessToken.of(accessTokenValue);

        // then
        assertThat(accessToken.getAccessToken()).isEqualTo(accessTokenValue);
    }
}