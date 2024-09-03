package com.taemin.user.exception;

import com.taemin.user.common.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends CustomException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
