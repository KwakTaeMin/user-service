package com.taemin.user.external;

import com.taemin.user.external.dto.response.NaverUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "naverOAuthClient", url = "https://openapi.naver.com/v1/nid")
public interface NaverOauthClient {

    @GetMapping("/me")
    NaverUserResponse getUserInfo(@RequestHeader("Authorization") String accessToken);
}
