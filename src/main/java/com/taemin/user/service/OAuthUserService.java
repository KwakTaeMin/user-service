package com.taemin.user.service;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.domain.user.OAuth2User;
import com.taemin.user.domain.user.OAuthToken;
import com.taemin.user.exception.UserException;
import com.taemin.user.external.GoogleOAuthClient;
import com.taemin.user.external.KakaoOauthClient;
import com.taemin.user.external.NaverOauthClient;
import com.taemin.user.external.dto.response.GoogleUserResponse;
import com.taemin.user.external.dto.response.KakaoUserResponse;
import com.taemin.user.external.dto.response.NaverUserResponse;
import com.taemin.user.type.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserService {

    private final GoogleOAuthClient googleOAuthClient;
    private final KakaoOauthClient kakaoOauthClient;
    private final NaverOauthClient naverOauthClient;

    public OAuth2User getOAuthUser(OAuthToken oauthToken) {
        if (oauthToken.getOAuthProvider().equals(OAuthProvider.GOOGLE)) {
            GoogleUserResponse googleUserResponse = googleOAuthClient.getUserInfo("Bearer " + oauthToken.getAccessToken());
            return OAuth2User.of(googleUserResponse);
        } else if (oauthToken.getOAuthProvider().equals(OAuthProvider.KAKAO)) {
            KakaoUserResponse kakaoUserResponse = kakaoOauthClient.getUserInfo("Bearer " + oauthToken.getAccessToken());
            return OAuth2User.of(kakaoUserResponse);
        } else if (oauthToken.getOAuthProvider().equals(OAuthProvider.NAVER)) {
            NaverUserResponse naverUserResponse = naverOauthClient.getUserInfo("Bearer " + oauthToken.getAccessToken());
            return OAuth2User.of(naverUserResponse);
        } else {
            throw new UserException(ErrorCode.ILLEGAL_OAUTH_PROVIDER);
        }
    }
}
