package com.taemin.user.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.taemin.user.domain.log.AccessType;
import com.taemin.user.domain.log.IP;
import com.taemin.user.domain.log.UserAgent;

import java.time.LocalDateTime;

public record AccessLogResponse(
        Long id,
        Long userId,
        AccessType accessType,
        boolean state,
        LocalDateTime accessAt,
        @JsonUnwrapped IP ip,
        @JsonUnwrapped UserAgent userAgent
) {
}
