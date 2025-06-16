package com.taemin.user.domain.token;

import com.taemin.user.domain.user.User;
import com.taemin.user.domain.user.Email;
import com.taemin.user.domain.user.Name;
import com.taemin.user.domain.user.Profile;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenTest {

    @Test
    @DisplayName("Token 생성시 정상적으로 생성되는지 확인")
    void createToken() {
        // given
        User user = User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );
        AccessToken accessToken = AccessToken.of("test-access-token");
        RefreshToken refreshToken = RefreshToken.of("test-refresh-token");

        // when
        Token token = Token.of(user, accessToken, refreshToken);

        // then
        assertThat(token.getUser()).isEqualTo(user);
        assertThat(token.getAccessToken()).isEqualTo(accessToken);
        assertThat(token.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("updateToken시 accessToken과 refreshToken이 모두 업데이트되는지 확인")
    void updateToken() {
        // given
        User user = User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );
        AccessToken accessToken = AccessToken.of("test-access-token");
        RefreshToken refreshToken = RefreshToken.of("test-refresh-token");
        Token token = Token.of(user, accessToken, refreshToken);

        AccessToken newAccessToken = AccessToken.of("new-access-token");
        RefreshToken newRefreshToken = RefreshToken.of("new-refresh-token");

        // when
        token.updateToken(newAccessToken, newRefreshToken);

        // then
        assertThat(token.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(token.getRefreshToken()).isEqualTo(newRefreshToken);
    }

    @Test
    @DisplayName("updateToken시 accessToken이 같을 때 refreshToken만 업데이트되는지 확인")
    void updateTokenWithSameAccessToken() {
        // given
        User user = User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );
        AccessToken accessToken = AccessToken.of("test-access-token");
        RefreshToken refreshToken = RefreshToken.of("test-refresh-token");
        Token token = Token.of(user, accessToken, refreshToken);

        RefreshToken newRefreshToken = RefreshToken.of("new-refresh-token");

        // when
        token.updateToken(accessToken, newRefreshToken);

        // then
        assertThat(token.getAccessToken()).isEqualTo(accessToken);
        assertThat(token.getRefreshToken()).isEqualTo(newRefreshToken);
    }

    @Test
    @DisplayName("updateAccessToken시 accessToken만 업데이트되는지 확인")
    void updateAccessToken() {
        // given
        User user = User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );
        AccessToken accessToken = AccessToken.of("test-access-token");
        RefreshToken refreshToken = RefreshToken.of("test-refresh-token");
        Token token = Token.of(user, accessToken, refreshToken);

        AccessToken newAccessToken = AccessToken.of("new-access-token");

        // when
        token.updateAccessToken(newAccessToken);

        // then
        assertThat(token.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(token.getRefreshToken()).isEqualTo(refreshToken);
    }
}