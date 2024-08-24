package com.taemin.user.exception;

import com.taemin.user.common.ErrorCode;
import lombok.Getter;

@Getter
public class AuthException extends CustomException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
