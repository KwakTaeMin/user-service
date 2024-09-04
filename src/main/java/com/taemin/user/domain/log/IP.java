package com.taemin.user.domain.log;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class IP {
    private String ip;

    private IP(String ip) {
        this.ip = ip;
    }

    public static IP of(String ip) {
        return new IP(ip);
    }
}
