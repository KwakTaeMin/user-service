package com.taemin.user.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // auth
    ILLEGAL_OAUTH_PROVIDER(NOT_ACCEPTABLE, "지원하지 않은 OAuth Provider 입니다."),
    TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "올바르지 않은 토큰입니다."),
    INVALID_JWT_SIGNATURE(UNAUTHORIZED, "잘못된 JWT 시그니처입니다."),
    USER_NOT_FOUND(BAD_REQUEST, "사용자를 찾을수 없습니다."),
    USER_NAME_VALIDATE_FAIL(BAD_REQUEST, "사용자의 이름(name) 유효하지 않습니다."),
    USER_EMAIL_VALIDATE_FAIL(BAD_REQUEST, "사용자의 이메일(email) 유효하지 않습니다."),
    USER_OAUTH_ID_VALIDATE_FAIL(BAD_REQUEST, "사용자의 OAuthId 유효하지 않습니다."),
    USER_PROFILE_VALIDATE_FAIL(BAD_REQUEST, "사용자의 Profile 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}