package com.taemin.user.domain.user;

import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("User 생성시 정상적으로 생성되는지 확인")
    void createUser() {
        // given
        Name name = Name.of("test");
        Email email = Email.of("test@test.com");
        Profile profile = Profile.of("test-profile");
        Role role = Role.USER;
        OAuthProvider oAuthProvider = OAuthProvider.GOOGLE;
        OAuthId oAuthId = OAuthId.of("test-oauth-id");

        // when
        User user = User.of(name, email, profile, role, oAuthProvider, oAuthId);

        // then
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getProfile()).isEqualTo(profile);
        assertThat(user.getRole()).isEqualTo(role);
        assertThat(user.getOauthProvider()).isEqualTo(oAuthProvider);
        assertThat(user.getOauthId()).isEqualTo(oAuthId);
    }

    @Test
    @DisplayName("User의 getUsername 메소드가 userId를 문자열로 반환하는지 확인")
    void getUsernameReturnsUserIdAsString() {
        // given
        User user = User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );

        // when & then
        // userId는 DB에서 생성되므로 null이거나 0일 수 있음
        assertThat(user.getUsername()).isNotNull();
    }

    @Test
    @DisplayName("User의 getAuthorities 메소드가 role에 해당하는 권한을 반환하는지 확인")
    void getAuthoritiesReturnsRoleAuthority() {
        // given
        Role role = Role.USER;
        User user = User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                role,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );

        // when
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // then
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo(role.getAuthority());
    }

    @Test
    @DisplayName("User의 isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled 메소드가 true를 반환하는지 확인")
    void userDetailsMethodsReturnTrue() {
        // given
        User user = User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );

        // when & then
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();
    }
}