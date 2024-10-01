package com.taemin.user.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.taemin.user.domain.log.AccessLog;
import com.taemin.user.domain.log.AccessType;
import com.taemin.user.domain.log.IP;
import com.taemin.user.domain.log.UserAgent;

import java.time.LocalDateTime;
import java.util.List;

public record AccessLogResponse(
        Long id,
        Long userId,
        AccessType accessType,
        boolean state,
        LocalDateTime accessAt,
        @JsonUnwrapped IP ip,
        @JsonUnwrapped UserAgent userAgent
) {
    public static List<AccessLogResponse> of(List<AccessLog> accessLogs) {
        return accessLogs.stream().map(accessLog ->
                new AccessLogResponse(accessLog.getId(), accessLog.getUser().getUserId(), accessLog.getAccessType(), accessLog.isState(), accessLog.getAccessAt(), accessLog.getIp(), accessLog.getUserAgent()))
                .toList();
    }
}