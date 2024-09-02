package com.taemin.user.domain;

import com.taemin.user.common.BaseEntity;
import com.taemin.user.convert.EncryptConverter;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    @Convert(converter = EncryptConverter.class)
    private String email;
    private String profile;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String oauthId;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OAuthProvider oauthProvider;

    @Builder
    public User(String name, String email, String profile, Role role, OAuthProvider oauthProvider, String oauthId) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.role = role;
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
    }
}
