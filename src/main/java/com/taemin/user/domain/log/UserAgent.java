package com.taemin.user.domain.log;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserAgent {
    private String userAgent;

    private UserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public static UserAgent of(String userAgent) {
        return new UserAgent(userAgent);
    }
}
