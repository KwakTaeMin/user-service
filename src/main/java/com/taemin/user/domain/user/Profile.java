package com.taemin.user.domain.user;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.exception.UserException;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Profile {
    private String profile;

    public Profile(String profile) {
        this.profile = profile;
    }

    public static Profile of(String profile) {
        validateProfile(profile);
        return new Profile(profile);
    }

    private static void validateProfile(String profile) {
        if(profile == null || profile.trim().isEmpty()) {
            throw new UserException(ErrorCode.USER_PROFILE_VALIDATE_FAIL);
        }
    }
}
