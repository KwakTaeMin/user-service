package com.taemin.user.domain.user;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.exception.UserException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Name {
    public static final int NAME_MAX_LENGTH = 50;

    @Column(nullable = false)
    private String name;

    private Name(String name) {
        this.name = name;
    }

    public static Name of(String name) {
        validateName(name);
        return new Name(name);
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new UserException(ErrorCode.USER_NAME_VALIDATE_FAIL);
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new UserException(ErrorCode.USER_NAME_VALIDATE_FAIL);
        }
    }
}
