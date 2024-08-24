package com.taemin.user.domain;

import com.taemin.user.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token extends BaseEntity { //redis 변경 필요

    @Id
    private String id;

    private String refreshToken;

    private String accessToken;

    public Token(String id, String refreshToken, String accessToken) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
