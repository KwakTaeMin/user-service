package com.taemin.user.external;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.exception.UserException;
import com.taemin.user.external.dto.response.OAuthUserResponse;
import com.taemin.user.type.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthClient {

    private final GoogleOAuthClient googleOAuthClient;
    private final NaverOauthClient naverOauthClient;
    private final KakaoOauthClient kakaoOauthClient;

    public OAuthUserResponse getOAuthUser(OAuthProvider oAuthProvider, String accessToken) {
        return switch (oAuthProvider) {
            case GOOGLE -> googleOAuthClient.getUserInfo(accessToken);
            case NAVER -> naverOauthClient.getUserInfo(accessToken);
            case KAKAO -> kakaoOauthClient.getUserInfo(accessToken);
            default -> throw new UserException(ErrorCode.ILLEGAL_OAUTH_PROVIDER);
        };
    }
}
