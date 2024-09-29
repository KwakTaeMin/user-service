package com.taemin.user.api;

import com.taemin.user.domain.log.AccessLog;
import com.taemin.user.domain.user.User;
import com.taemin.user.dto.response.AccessLogResponse;
import com.taemin.user.dto.response.UserResponse;
import com.taemin.user.service.AccessLogService;
import com.taemin.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AccessLogService accessLogService;

    @GetMapping("/current")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserById(Long.parseLong(userDetails.getUsername()));
        logger.info("Currently logged in user: {}", currentUser);
        return ResponseEntity.ok(UserResponse.of(currentUser));
    }

    @GetMapping("/access/log")
    public ResponseEntity<List<AccessLogResponse>> getAccessLog(@AuthenticationPrincipal UserDetails userDetails) {
        List<AccessLog> accessLogs = accessLogService.getAccessLogs(Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(AccessLogResponse.of(accessLogs));
    }
}

