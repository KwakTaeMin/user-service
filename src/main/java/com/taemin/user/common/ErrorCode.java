package com.taemin.user.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // auth
    ILLEGAL_REGISTRATION_ID(NOT_ACCEPTABLE, "illegal registration id"),
    TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "올바르지 않은 토큰입니다."),
    INVALID_JWT_SIGNATURE(UNAUTHORIZED, "잘못된 JWT 시그니처입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}