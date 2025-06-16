package com.taemin.user.domain.user;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OAuthIdTest {

    @Test
    @DisplayName("OAuthId 생성시 정상적으로 생성되는지 확인")
    void createOAuthId() {
        // given
        String oauthIdValue = "test-oauth-id";

        // when
        OAuthId oAuthId = OAuthId.of(oauthIdValue);

        // then
        assertThat(oAuthId.getOauthId()).isEqualTo(oauthIdValue);
    }

    @Test
    @DisplayName("OAuthId 생성시 null이면 예외가 발생하는지 확인")
    void createOAuthIdWithNullValue() {
        // when & then
        assertThatThrownBy(() -> OAuthId.of(null))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_OAUTH_ID_VALIDATE_FAIL);
    }

    @Test
    @DisplayName("OAuthId 생성시 빈 문자열이면 예외가 발생하는지 확인")
    void createOAuthIdWithEmptyValue() {
        // when & then
        assertThatThrownBy(() -> OAuthId.of(""))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_OAUTH_ID_VALIDATE_FAIL);
    }

    @Test
    @DisplayName("OAuthId 생성시 공백 문자열이면 예외가 발생하는지 확인")
    void createOAuthIdWithBlankValue() {
        // when & then
        assertThatThrownBy(() -> OAuthId.of("   "))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_OAUTH_ID_VALIDATE_FAIL);
    }
}