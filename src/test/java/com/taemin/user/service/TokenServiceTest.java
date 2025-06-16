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
import com.taemin.user.repository.TokenRepository;
import com.taemin.user.repository.UserRepository;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.taemin.user.common.ErrorCode.TOKEN_EXPIRED;
import static com.taemin.user.common.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test
    @DisplayName("사용자 ID로 토큰을 정상적으로 삭제하는지 확인")
    void deleteToken() {
        // given
        User user = createUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        tokenService.deleteToken(1L);

        // then
        verify(tokenRepository).deleteByUser(user);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 토큰 삭제시 예외가 발생하는지 확인")
    void deleteTokenWithNonExistentUser() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tokenService.deleteToken(999L))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", USER_NOT_FOUND);
    }

    @Test
    @DisplayName("토큰이 존재하지 않을 때 새로운 토큰을 저장하는지 확인")
    void saveNewToken() {
        // given
        User user = createUser();
        AccessToken accessToken = AccessToken.of("test-access-token");
        RefreshToken refreshToken = RefreshToken.of("test-refresh-token");
        
        when(tokenRepository.findById(user.getUserId())).thenReturn(Optional.empty());
        
        // when
        tokenService.saveOrUpdate(user, accessToken, refreshToken);
        
        // then
        verify(tokenRepository).save(any(Token.class));
    }
    
    @Test
    @DisplayName("토큰이 이미 존재할 때 토큰을 업데이트하는지 확인")
    void updateExistingToken() {
        // given
        User user = createUser();
        AccessToken accessToken = AccessToken.of("test-access-token");
        RefreshToken refreshToken = RefreshToken.of("test-refresh-token");
        Token existingToken = Token.of(user, AccessToken.of("old-access-token"), RefreshToken.of("old-refresh-token"));
        
        when(tokenRepository.findById(user.getUserId())).thenReturn(Optional.of(existingToken));
        
        // when
        tokenService.saveOrUpdate(user, accessToken, refreshToken);
        
        // then
        verify(tokenRepository).save(existingToken);
        assertThat(existingToken.getAccessToken()).isEqualTo(accessToken);
        assertThat(existingToken.getRefreshToken()).isEqualTo(refreshToken);
    }
    
    @Test
    @DisplayName("액세스 토큰으로 토큰을 찾을 수 있는지 확인")
    void findByAccessTokenOrThrow() {
        // given
        User user = createUser();
        AccessToken accessToken = AccessToken.of("test-access-token");
        RefreshToken refreshToken = RefreshToken.of("test-refresh-token");
        Token token = Token.of(user, accessToken, refreshToken);
        
        when(tokenRepository.findByAccessToken(accessToken)).thenReturn(Optional.of(token));
        
        // when
        Token foundToken = tokenService.findByAccessTokenOrThrow(accessToken);
        
        // then
        assertThat(foundToken).isEqualTo(token);
    }
    
    @Test
    @DisplayName("존재하지 않는 액세스 토큰으로 토큰 조회시 예외가 발생하는지 확인")
    void findByNonExistentAccessToken() {
        // given
        AccessToken accessToken = AccessToken.of("non-existent-token");
        when(tokenRepository.findByAccessToken(accessToken)).thenReturn(Optional.empty());
        
        // when & then
        assertThatThrownBy(() -> tokenService.findByAccessTokenOrThrow(accessToken))
                .isInstanceOf(TokenException.class)
                .hasFieldOrPropertyWithValue("errorCode", TOKEN_EXPIRED);
    }
    
    @Test
    @DisplayName("토큰의 액세스 토큰만 업데이트되는지 확인")
    void updateToken() {
        // given
        User user = createUser();
        AccessToken oldAccessToken = AccessToken.of("old-access-token");
        RefreshToken refreshToken = RefreshToken.of("test-refresh-token");
        Token token = Token.of(user, oldAccessToken, refreshToken);
        
        AccessToken newAccessToken = AccessToken.of("new-access-token");
        
        // when
        tokenService.updateToken(token, newAccessToken);
        
        // then
        verify(tokenRepository).save(token);
        assertThat(token.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(token.getRefreshToken()).isEqualTo(refreshToken);
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