package com.taemin.user.service;

import com.taemin.user.domain.log.AccessLog;
import com.taemin.user.domain.log.AccessType;
import com.taemin.user.domain.log.IP;
import com.taemin.user.domain.log.UserAgent;
import com.taemin.user.domain.user.Email;
import com.taemin.user.domain.user.Name;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.Profile;
import com.taemin.user.domain.user.User;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.AccessLogRepository;
import com.taemin.user.repository.UserRepository;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.taemin.user.common.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessLogServiceTest {

    @Mock
    private AccessLogRepository accessLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccessLogService accessLogService;

    @Test
    @DisplayName("사용자 ID로 접근 로그를 정상적으로 조회하는지 확인")
    void getAccessLogs() {
        // given
        User user = createUser();
        IP ip = IP.of("127.0.0.1");
        UserAgent userAgent = UserAgent.of("Mozilla/5.0");
        List<AccessLog> expectedLogs = List.of(
                AccessLog.ofLogin(user, ip, userAgent),
                AccessLog.ofLogin(user, ip, userAgent)
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accessLogRepository.findByUser(user)).thenReturn(expectedLogs);

        // when
        List<AccessLog> actualLogs = accessLogService.getAccessLogs(1L);

        // then
        assertThat(actualLogs).isEqualTo(expectedLogs);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 접근 로그 조회시 예외가 발생하는지 확인")
    void getAccessLogsWithNonExistentUser() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> accessLogService.getAccessLogs(999L))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", USER_NOT_FOUND);
    }

    private User createUser() {
        return User.of(
                Name.of("test"),
                Email.of("test@test.com"),
                Profile.of("test-profile"),
                Role.USER,
                OAuthProvider.GOOGLE,
                OAuthId.of("test-oauth-id")
        );
    }
}
