package com.taemin.user.service;

import com.taemin.user.domain.user.OAuth2User;
import com.taemin.user.domain.user.OAuthToken;
import com.taemin.user.external.OAuthClient;
import com.taemin.user.external.dto.response.OAuthUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserService {

    public static final String BEARER = "Bearer ";
    private final OAuthClient oAuthClient;

    public OAuth2User getOAuthUser(OAuthToken oauthToken) {
        OAuthUserResponse oAuthUserResponse = oAuthClient.getOAuthUser(oauthToken.getOAuthProvider(), BEARER + oauthToken.getAccessToken());
        return OAuth2User.of(oAuthUserResponse);
    }
}
