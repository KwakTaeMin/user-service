package com.taemin.user.service;

import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.domain.user.Email;
import com.taemin.user.domain.user.Name;
import com.taemin.user.domain.user.OAuth2User;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.OAuthToken;
import com.taemin.user.domain.user.Profile;
import com.taemin.user.domain.user.User;
import com.taemin.user.dto.request.LoginRequest;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.UserRepository;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.taemin.user.common.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private OAuthUserService oAuthUserService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자 ID로 사용자 정보를 정상적으로 로드하는지 확인")
    void loadUserByUsername() {
        // given
        User user = createUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        UserDetails userDetails = userService.loadUserByUsername("1");

        // then
        assertThat(userDetails).isEqualTo(user);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 사용자 정보 로드시 예외가 발생하는지 확인")
    void loadUserByUsernameWithNonExistentUser() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.loadUserByUsername("999"))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", USER_NOT_FOUND);
    }

    @Test
    @DisplayName("로그인 요청시 기존 사용자가 있으면 해당 사용자를 반환하는지 확인")
    void loginWithExistingUser() {
        // given
        OAuthToken oAuthToken = OAuthToken.of("test-access-token", "test-refresh-token", OAuthProvider.GOOGLE);
        LoginRequest loginRequest = new LoginRequest(oAuthToken.getOAuthProvider(), oAuthToken.getAccessToken(), oAuthToken.getRefreshToken());

        OAuth2User oAuth2User = new OAuth2User("test-oauth-id", "test-name", "test@test.com", "test-profile", OAuthProvider.GOOGLE);
        User existingUser = createUser();
        OAuthId oAuthId = OAuthId.of("test-oauth-id");
        AccessToken accessToken = AccessToken.of("generated-access-token");

        when(oAuthUserService.getOAuthUser(any(OAuthToken.class))).thenReturn(oAuth2User);
        when(userRepository.findByOauthId(oAuthId)).thenReturn(Optional.of(existingUser));
        when(tokenProvider.generateToken(existingUser)).thenReturn(accessToken);

        // when
        AccessToken result = userService.login(loginRequest);

        // then
        assertThat(result).isEqualTo(accessToken);
        verify(tokenProvider).refreshToken(existingUser, accessToken);
        verify(eventPublisher).publishEvent(any(AuthenticationSuccessEvent.class));
    }

    @Test
    @DisplayName("로그인 요청시 새로운 사용자면 사용자를 생성하는지 확인")
    void loginWithNewUser() {
        // given
        OAuthToken oAuthToken = OAuthToken.of("test-access-token", "test-refresh-token", OAuthProvider.GOOGLE);
        LoginRequest loginRequest = new LoginRequest(oAuthToken.getOAuthProvider(), oAuthToken.getAccessToken(), oAuthToken.getRefreshToken());

        OAuth2User oAuth2User = new OAuth2User("test-oauth-id", "test-name", "test@test.com", "test-profile", OAuthProvider.GOOGLE);
        User newUser = createUser();
        OAuthId oAuthId = OAuthId.of("test-oauth-id");
        AccessToken accessToken = AccessToken.of("generated-access-token");

        when(oAuthUserService.getOAuthUser(any(OAuthToken.class))).thenReturn(oAuth2User);
        when(userRepository.findByOauthId(oAuthId)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(tokenProvider.generateToken(newUser)).thenReturn(accessToken);

        // when
        AccessToken result = userService.login(loginRequest);

        // then
        assertThat(result).isEqualTo(accessToken);
        verify(userRepository).save(any(User.class));
        verify(tokenProvider).refreshToken(newUser, accessToken);
        verify(eventPublisher).publishEvent(any(AuthenticationSuccessEvent.class));
    }

    @Test
    @DisplayName("사용자 ID로 사용자를 정상적으로 조회하는지 확인")
    void getUserById() {
        // given
        User user = createUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        User result = userService.getUserById(1L);

        // then
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 사용자 조회시 예외가 발생하는지 확인")
    void getUserByIdWithNonExistentUser() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", USER_NOT_FOUND);
    }

    private User createUser() {
        return User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );
    }
}
