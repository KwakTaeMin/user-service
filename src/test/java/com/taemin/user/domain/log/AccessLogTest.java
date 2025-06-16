package com.taemin.user.domain.log;

import com.taemin.user.domain.user.User;
import com.taemin.user.domain.user.Email;
import com.taemin.user.domain.user.Name;
import com.taemin.user.domain.user.Profile;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccessLogTest {

    @Test
    @DisplayName("로그인 AccessLog 생성시 정상적으로 생성되는지 확인")
    void createLoginAccessLog() {
        // given
        User user = User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );
        IP ip = IP.of("127.0.0.1");
        UserAgent userAgent = UserAgent.of("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        // when
        AccessLog accessLog = AccessLog.ofLogin(user, ip, userAgent);

        // then
        assertThat(accessLog.getUser()).isEqualTo(user);
        assertThat(accessLog.getAccessType()).isEqualTo(AccessType.LOGIN);
        assertThat(accessLog.isState()).isTrue();
        assertThat(accessLog.getAccessAt()).isNotNull();
        assertThat(accessLog.getIp()).isEqualTo(ip);
        assertThat(accessLog.getUserAgent()).isEqualTo(userAgent);
    }
}