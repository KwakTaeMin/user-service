package com.taemin.user.domain.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IPTest {

    @Test
    @DisplayName("IP 생성시 정상적으로 생성되는지 확인")
    void createIP() {
        // given
        String ipValue = "127.0.0.1";

        // when
        IP ip = IP.of(ipValue);

        // then
        assertThat(ip.getIp()).isEqualTo(ipValue);
    }
}