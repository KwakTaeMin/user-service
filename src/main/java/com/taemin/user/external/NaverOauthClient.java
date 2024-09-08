package com.taemin.user.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(name = "naverOAuthClient", url = "https://www.googleapis.com/oauth2/v3")
public interface NaverOauthClient {
}
