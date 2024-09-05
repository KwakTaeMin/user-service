package com.taemin.user.domain.user;


import com.taemin.user.common.ErrorCode;
import com.taemin.user.convert.EncryptConverter;
import com.taemin.user.exception.UserException;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Email {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Column(nullable = false)
    @Convert(converter = EncryptConverter.class)
    private String email;

    private Email(String email) {
        this.email = email;
    }

    public static Email of(String email) {
        validateEmail(email);
        return new Email(email);
    }

    private static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return;
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new UserException(ErrorCode.USER_EMAIL_VALIDATE_FAIL);
        }
    }
}
