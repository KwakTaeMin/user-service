package com.taemin.userservice.user.domain;

import com.taemin.userservice.common.BaseEntity;
import com.taemin.userservice.user.type.OAuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "oauth_provider"})})
public class UserOAuth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userOAuthId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider", nullable = false)
    private OAuthProvider oAuthProvider;
    private String providerUserId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpirationAt;

    public UserOAuth(User user, OAuthProvider oAuthProvider, String providerUserId, String accessToken, String refreshToken,
                     LocalDateTime tokenExpirationAt) {
        this.user = user;
        this.oAuthProvider = oAuthProvider;
        this.providerUserId = providerUserId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenExpirationAt = tokenExpirationAt;
    }
}
