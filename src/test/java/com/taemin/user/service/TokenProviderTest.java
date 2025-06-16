package com.taemin.user.service;

import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.domain.token.RefreshToken;
import com.taemin.user.domain.token.Token;
import com.taemin.user.domain.user.Email;
import com.taemin.user.domain.user.Name;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.Profile;
import com.taemin.user.domain.user.User;
import com.taemin.user.exception.TokenException;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.UserRepository;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Optional;

import static com.taemin.user.common.ErrorCode.INVALID_JWT_SIGNATURE;
import static com.taemin.user.common.ErrorCode.INVALID_TOKEN;
import static com.taemin.user.common.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TokenProvider tokenProvider;

    private User user;
    private final String testKey = "test-jwt-key-that-is-long-enough-for-hmac-sha512-algorithm-with-at-least-512-bits-of-length-to-satisfy-security-requirements-according-to-rfc7518-section-3.2";

    @BeforeEach
    void setUp() {
        user = createUser();
        ReflectionTestUtils.setField(tokenProvider, "key", testKey);
        ReflectionTestUtils.invokeMethod(tokenProvider, "setSecretKey");
    }

    @Test
    @DisplayName("사용자 정보로 액세스 토큰을 정상적으로 생성하는지 확인")
    void generateToken() {
        // when
        AccessToken accessToken = tokenProvider.generateToken(user);

        // then
        assertThat(accessToken).isNotNull();
        assertThat(accessToken.getAccessToken()).isNotEmpty();
    }

    @Test
    @DisplayName("사용자와 액세스 토큰으로 리프레시 토큰을 정상적으로 생성하고 저장하는지 확인")
    void refreshToken() {
        // given
        AccessToken accessToken = AccessToken.of("test-access-token");

        // when
        tokenProvider.refreshToken(user, accessToken);

        // then
        verify(tokenService).saveOrUpdate(any(User.class), any(AccessToken.class), any(RefreshToken.class));
    }

    @Test
    @DisplayName("토큰으로 사용자를 정상적으로 조회하는지 확인")
    void getUserByToken() {
        // given
        AccessToken accessToken = tokenProvider.generateToken(user);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        User foundUser = tokenProvider.getUserByToken(accessToken.getAccessToken());

        // then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID의 토큰으로 사용자 조회시 예외가 발생하는지 확인")
    void getUserByTokenWithNonExistentUser() {
        // given
        User nonExistentUser = User.of(
                Name.of("non-existent"),
                Email.of("non-existent@test.com"),
                Profile.of("non-existent-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("non-existent-oauth-id")
        );
        ReflectionTestUtils.setField(nonExistentUser, "userId", 999L);

        AccessToken accessToken = tokenProvider.generateToken(nonExistentUser);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tokenProvider.getUserByToken(accessToken.getAccessToken()))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", USER_NOT_FOUND);
    }

    @Test
    @DisplayName("액세스 토큰으로 새로운 액세스 토큰을 정상적으로 재발급하는지 확인")
    void reissueAccessToken() {
        // given
        AccessToken accessToken = AccessToken.of("test-access-token");
        RefreshToken refreshToken = RefreshToken.of(tokenProvider.generateToken(user).getAccessToken());
        Token token = Token.of(user, accessToken, refreshToken);

        when(tokenService.findByAccessTokenOrThrow(accessToken)).thenReturn(token);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        AccessToken reissuedToken = tokenProvider.reissueAccessToken(accessToken);

        // then
        assertThat(reissuedToken).isNotNull();
        verify(tokenService).updateToken(token, reissuedToken);
    }

    @Test
    @DisplayName("유효한 토큰을 정상적으로 검증하는지 확인")
    void validateValidToken() {
        // given
        AccessToken accessToken = tokenProvider.generateToken(user);

        // when
        boolean isValid = tokenProvider.validateToken(accessToken.getAccessToken());

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("만료된 토큰을 검증시 false를 반환하는지 확인")
    void validateExpiredToken() {
        // given
        // Use reflection to access the private generateToken method with an expired date
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() - 1000); // 1 second in the past

        String expiredToken = ReflectionTestUtils.invokeMethod(tokenProvider, "generateToken", 
                user, 
                -1000L); // negative time means token is already expired

        // when
        boolean isValid = tokenProvider.validateToken(expiredToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("빈 토큰을 검증시 false를 반환하는지 확인")
    void validateEmptyToken() {
        // when
        boolean isValid = tokenProvider.validateToken("");

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("잘못된 형식의 토큰을 검증시 예외가 발생하는지 확인")
    void validateMalformedToken() {
        // given
        String malformedToken = "invalid-token-format";

        // when & then
        assertThatThrownBy(() -> tokenProvider.validateToken(malformedToken))
                .isInstanceOf(TokenException.class)
                .hasFieldOrPropertyWithValue("errorCode", INVALID_TOKEN);
    }

    private User createUser() {
        User user = User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );
        ReflectionTestUtils.setField(user, "userId", 1L);
        return user;
    }
}
