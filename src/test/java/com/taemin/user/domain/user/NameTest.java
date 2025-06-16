package com.taemin.user.domain.user;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {

    @Test
    @DisplayName("Name 생성시 정상적으로 생성되는지 확인")
    void createName() {
        // given
        String nameValue = "test";

        // when
        Name name = Name.of(nameValue);

        // then
        assertThat(name.getName()).isEqualTo(nameValue);
    }

    @Test
    @DisplayName("Name 생성시 null이면 예외가 발생하는지 확인")
    void createNameWithNullValue() {
        // when & then
        assertThatThrownBy(() -> Name.of(null))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NAME_VALIDATE_FAIL);
    }

    @Test
    @DisplayName("Name 생성시 빈 문자열이면 예외가 발생하는지 확인")
    void createNameWithEmptyValue() {
        // when & then
        assertThatThrownBy(() -> Name.of(""))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NAME_VALIDATE_FAIL);
    }

    @Test
    @DisplayName("Name 생성시 공백 문자열이면 예외가 발생하는지 확인")
    void createNameWithBlankValue() {
        // when & then
        assertThatThrownBy(() -> Name.of("   "))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NAME_VALIDATE_FAIL);
    }

    @Test
    @DisplayName("Name 생성시 최대 길이를 초과하면 예외가 발생하는지 확인")
    void createNameWithTooLongValue() {
        // given
        String tooLongName = "a".repeat(Name.NAME_MAX_LENGTH + 1);

        // when & then
        assertThatThrownBy(() -> Name.of(tooLongName))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NAME_VALIDATE_FAIL);
    }
}