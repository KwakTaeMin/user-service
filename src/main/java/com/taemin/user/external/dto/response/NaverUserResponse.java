package com.taemin.user.external.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NaverUserResponse {
    private String resultcode;
    private String message;
    private Response response;

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String email;
        private String name;
        private String profileImage;
    }
}
