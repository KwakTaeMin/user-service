package com.taemin.user.service;

import com.taemin.user.domain.user.OAuth2User;
import com.taemin.user.domain.user.OAuthToken;
import com.taemin.user.external.OAuthClient;
import com.taemin.user.external.dto.response.OAuthUserResponse;
import com.taemin.user.type.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuthUserServiceTest {

    @Mock
    private OAuthClient oAuthClient;

    @InjectMocks
    private OAuthUserService oAuthUserService;

    @Test
    @DisplayName("OAuth 토큰으로 사용자 정보를 정상적으로 가져오는지 확인")
    void getOAuthUser() {
        // given
        OAuthToken oAuthToken = OAuthToken.of("test-access-token", "test-refresh-token", OAuthProvider.GOOGLE);

        OAuthUserResponse oAuthUserResponse = Mockito.mock(OAuthUserResponse.class);
        when(oAuthUserResponse.getId()).thenReturn("test-oauth-id");
        when(oAuthUserResponse.getName()).thenReturn("test-name");
        when(oAuthUserResponse.getEmail()).thenReturn("test@test.com");
        when(oAuthUserResponse.getProfileImage()).thenReturn("test-profile");
        when(oAuthUserResponse.getOAuthProvider()).thenReturn(OAuthProvider.GOOGLE);

        when(oAuthClient.getOAuthUser(oAuthToken.getOAuthProvider(), "Bearer " + oAuthToken.getAccessToken()))
                .thenReturn(oAuthUserResponse);

        // when
        OAuth2User oAuth2User = oAuthUserService.getOAuthUser(oAuthToken);

        // then
        assertThat(oAuth2User.oAuthId()).isEqualTo("test-oauth-id");
        assertThat(oAuth2User.name()).isEqualTo("test-name");
        assertThat(oAuth2User.email()).isEqualTo("test@test.com");
        assertThat(oAuth2User.profile()).isEqualTo("test-profile");
    }
}
