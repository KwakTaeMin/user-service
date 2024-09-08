package com.taemin.user.service;

import com.taemin.user.domain.user.OAuth2User;
import com.taemin.user.domain.user.OAuthToken;
import com.taemin.user.external.GoogleOAuthClient;
import com.taemin.user.external.KakaoOauthClient;
import com.taemin.user.external.NaverOauthClient;
import com.taemin.user.external.dto.response.GoogleUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserService {

    private final GoogleOAuthClient googleOAuthClient;
    private final KakaoOauthClient kakaoOauthClient;
    private final NaverOauthClient naverOauthClient;

    public OAuth2User getOAuthUser(OAuthToken oauthToken) {
        GoogleUserResponse googleUserResponse = googleOAuthClient.getUserInfo("Bearer " + oauthToken.getAccessToken());
        return OAuth2User.of(googleUserResponse);
    }
}
