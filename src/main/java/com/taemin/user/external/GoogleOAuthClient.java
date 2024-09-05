package com.taemin.user.external;

import com.taemin.user.external.dto.response.GoogleUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "googleOAuthClient", url = "https://www.googleapis.com/oauth2/v3")
public interface GoogleOAuthClient {

    @GetMapping("/userinfo")
    GoogleUserResponse getUserInfo(@RequestHeader("Authorization") String accessToken);
}