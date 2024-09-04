package com.taemin.user.domain.token;

import com.taemin.user.common.BaseEntity;
import com.taemin.user.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private RefreshToken refreshToken;

    @Column(nullable = false)
    private AccessToken accessToken;

    private Token(User user, AccessToken accessToken, RefreshToken refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public static Token of(User user, AccessToken accessToken, RefreshToken refreshToken) {
        return new Token(user, accessToken, refreshToken);
    }

    public void updateToken(AccessToken accessToken, RefreshToken refreshToken) {
        if (!Objects.equals(this.accessToken, accessToken)) {
            this.accessToken = accessToken;
        }
        this.refreshToken = refreshToken;
    }

    public void updateAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}
