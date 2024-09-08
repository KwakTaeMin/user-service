package com.taemin.user.external.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverUserResponse {
    private String sub;
    private String name;
    private String picture;
    private String email;

    public NaverUserResponse(String sub, String name, String picture, String email) {
        this.sub = sub;
        this.name = name;
        this.picture = picture;
        this.email = email;
    }
}
