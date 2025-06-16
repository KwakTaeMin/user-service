package com.taemin.user.domain.user;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    @DisplayName("Email 생성시 정상적으로 생성되는지 확인")
    void createEmail() {
        // given
        String emailValue = "test@test.com";

        // when
        Email email = Email.of(emailValue);

        // then
        assertThat(email.getEmail()).isEqualTo(emailValue);
    }

    @Test
    @DisplayName("Email 생성시 null이면 정상적으로 생성되는지 확인")
    void createEmailWithNullValue() {
        // when
        Email email = Email.of(null);

        // then
        assertThat(email.getEmail()).isNull();
    }

    @Test
    @DisplayName("Email 생성시 빈 문자열이면 정상적으로 생성되는지 확인")
    void createEmailWithEmptyValue() {
        // when
        Email email = Email.of("");

        // then
        assertThat(email.getEmail()).isEmpty();
    }

    @Test
    @DisplayName("Email 생성시 이메일 형식이 아니면 예외가 발생하는지 확인")
    void createEmailWithInvalidFormat() {
        // when & then
        assertThatThrownBy(() -> Email.of("invalid-email"))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_EMAIL_VALIDATE_FAIL);
    }
}