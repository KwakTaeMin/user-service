package com.taemin.user.listener;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.domain.log.AccessLog;
import com.taemin.user.domain.log.IP;
import com.taemin.user.domain.log.UserAgent;
import com.taemin.user.domain.user.User;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.AccessLogRepository;
import com.taemin.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthenticationEventListener {

    public static final String USER_AGENT_HEADER_KEY = "User-Agent";
    private final UserRepository userRepository;
    private final AccessLogRepository accessLogRepository;

    @EventListener //todo : OAuth Login 시 Event 발생하는데 OAuth Login은 Front로 넘겨서 Back-end는 Event를 만들어야한다.
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        WebAuthenticationDetails authDetails = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        String userId = userDetails.getUsername();
        String ipAddress = authDetails.getRemoteAddress();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String userAgent = request.getHeader(USER_AGENT_HEADER_KEY);

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        accessLogRepository.save(AccessLog.ofLogin(user, IP.of(ipAddress), UserAgent.of(userAgent)));
    }
}
