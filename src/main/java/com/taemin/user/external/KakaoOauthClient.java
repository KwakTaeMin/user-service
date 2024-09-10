package com.taemin.user.external;

import com.taemin.user.external.dto.response.KakaoUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "kakaoOAuthClient", url = "https://kapi.kakao.com/v2/")
public interface KakaoOauthClient {

    @GetMapping("/user/me")
    KakaoUserResponse getUserInfo(@RequestHeader("Authorization") String accessToken);
}
