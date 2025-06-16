package com.taemin.user.domain.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserAgentTest {

    @Test
    @DisplayName("UserAgent 생성시 정상적으로 생성되는지 확인")
    void createUserAgent() {
        // given
        String userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

        // when
        UserAgent userAgent = UserAgent.of(userAgentString);

        // then
        assertThat(userAgent.getBrowserFamily()).isEqualTo("Chrome");
        assertThat(userAgent.getBrowserVersion()).contains("91.0");
        assertThat(userAgent.getOsFamily()).isEqualTo("Windows");
        assertThat(userAgent.getOsVersion()).contains("10");
        assertThat(userAgent.getDeviceFamily()).isNotNull();
    }
}