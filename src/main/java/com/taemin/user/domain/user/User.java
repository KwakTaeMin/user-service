package com.taemin.user.domain.user;

import com.taemin.user.common.BaseEntity;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private Name name;
    private Email email;
    private Profile profile;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private OAuthId oauthId;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OAuthProvider oauthProvider;

    private User(Name name, Email email, Profile profile, Role role, OAuthProvider oauthProvider, OAuthId oauthId) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.role = role;
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
    }

    public static User of(Name name, Email email, Profile profile, Role role, OAuthProvider oauthProvider, OAuthId oauthId) {
        return new User(name, email, profile, role, oauthProvider, oauthId);
    }

    @Override
    public String getUsername() {
        return String.valueOf(userId);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getAuthority()));
    }
}
