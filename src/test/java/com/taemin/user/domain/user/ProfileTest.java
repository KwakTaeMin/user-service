package com.taemin.user.domain.user;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    @Test
    @DisplayName("Profile 생성시 정상적으로 생성되는지 확인")
    void createProfile() {
        // given
        String profileValue = "test-profile";

        // when
        Profile profile = Profile.of(profileValue);

        // then
        assertThat(profile.getProfile()).isEqualTo(profileValue);
    }

    @Test
    @DisplayName("Profile 생성시 null이면 예외가 발생하는지 확인")
    void createProfileWithNullValue() {
        // when & then
        assertThatThrownBy(() -> Profile.of(null))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_PROFILE_VALIDATE_FAIL);
    }

    @Test
    @DisplayName("Profile 생성시 빈 문자열이면 예외가 발생하는지 확인")
    void createProfileWithEmptyValue() {
        // when & then
        assertThatThrownBy(() -> Profile.of(""))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_PROFILE_VALIDATE_FAIL);
    }

    @Test
    @DisplayName("Profile 생성시 공백 문자열이면 예외가 발생하는지 확인")
    void createProfileWithBlankValue() {
        // when & then
        assertThatThrownBy(() -> Profile.of("   "))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_PROFILE_VALIDATE_FAIL);
    }
}