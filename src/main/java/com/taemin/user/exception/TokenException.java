package com.taemin.user.exception;

import com.taemin.user.common.ErrorCode;
import lombok.Getter;

@Getter
public class TokenException extends CustomException {
    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
