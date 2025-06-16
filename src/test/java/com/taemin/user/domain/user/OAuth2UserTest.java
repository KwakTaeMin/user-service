package com.taemin.user.domain.user;

import com.taemin.user.external.dto.response.OAuthUserResponse;
import com.taemin.user.type.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class OAuth2UserTest {

    @Test
    @DisplayName("OAuth2User 생성시 정상적으로 생성되는지 확인")
    void createOAuth2User() {
        // given
        String oAuthId = "test-oauth-id";
        String name = "test";
        String email = "test@test.com";
        String profile = "test-profile";
        OAuthProvider oAuthProvider = OAuthProvider.GOOGLE;

        OAuthUserResponse oAuthUserResponse = Mockito.mock(OAuthUserResponse.class);
        when(oAuthUserResponse.getId()).thenReturn(oAuthId);
        when(oAuthUserResponse.getName()).thenReturn(name);
        when(oAuthUserResponse.getEmail()).thenReturn(email);
        when(oAuthUserResponse.getProfileImage()).thenReturn(profile);
        when(oAuthUserResponse.getOAuthProvider()).thenReturn(oAuthProvider);

        // when
        OAuth2User oAuth2User = OAuth2User.of(oAuthUserResponse);

        // then
        assertThat(oAuth2User.oAuthId()).isEqualTo(oAuthId);
        assertThat(oAuth2User.name()).isEqualTo(name);
        assertThat(oAuth2User.email()).isEqualTo(email);
        assertThat(oAuth2User.profile()).isEqualTo(profile);
        assertThat(oAuth2User.oAuthProvider()).isEqualTo(oAuthProvider);
    }

    @Test
    @DisplayName("OAuth2User의 toEntity 메소드가 User 엔티티로 변환되는지 확인")
    void toEntityConvertsToUserEntity() {
        // given
        String oAuthId = "test-oauth-id";
        String name = "test";
        String email = "test@test.com";
        String profile = "test-profile";
        OAuthProvider oAuthProvider = OAuthProvider.GOOGLE;

        OAuth2User oAuth2User = new OAuth2User(oAuthId, name, email, profile, oAuthProvider);

        // when
        User user = oAuth2User.toEntity();

        // then
        assertThat(user.getName().getName()).isEqualTo(name);
        assertThat(user.getEmail().getEmail()).isEqualTo(email);
        assertThat(user.getProfile().getProfile()).isEqualTo(profile);
        assertThat(user.getOauthId().getOauthId()).isEqualTo(oAuthId);
        assertThat(user.getOauthProvider()).isEqualTo(oAuthProvider);
    }
}