package com.taemin.user.service;


import com.taemin.user.domain.user.OAuth2UserInfo;
import com.taemin.user.common.PrincipalDetails;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.User;
import com.taemin.user.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
        User user = getOrSave(registrationId, oAuth2UserInfo);
        return new PrincipalDetails(user, oAuth2UserAttributes, userNameAttributeName);
    }

    private User getOrSave(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        User user = userRepository.findByOauthId(OAuthId.of(oAuth2UserInfo.oAuthId()))
            .orElseGet(() -> oAuth2UserInfo.toEntity(registrationId));
        return userRepository.save(user);
    }
}
