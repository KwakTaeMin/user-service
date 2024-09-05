package com.taemin.user.external.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleUserResponse {
    private String sub;
    private String name;
    private String picture;
    private String email;

    public GoogleUserResponse(String sub, String name, String picture, String email) {
        this.sub = sub;
        this.name = name;
        this.picture = picture;
        this.email = email;
    }
}
