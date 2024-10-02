package com.taemin.user.service;

import com.taemin.user.domain.log.AccessLog;
import com.taemin.user.domain.user.User;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.AccessLogRepository;
import com.taemin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.taemin.user.common.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogRepository accessLogRepository;
    private final UserRepository userRepository;

    public List<AccessLog> getAccessLogs(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));// todo front 확인
        return accessLogRepository.findByUser(user);
    }
}
