package com.taemin.user.service;


import com.taemin.user.common.PrincipalDetails;
import com.taemin.user.domain.user.OAuth2User;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.User;
import com.taemin.user.repository.UserRepository;
import com.taemin.user.type.OAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public org.springframework.security.oauth2.core.user.OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        OAuthProvider oAuthProvider = OAuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        OAuth2User oAuth2User = OAuth2User.of(oAuthProvider, oAuth2UserAttributes);
        User user = getOrSave(oAuthProvider, oAuth2User);
        return new PrincipalDetails(user, oAuth2UserAttributes, userNameAttributeName);
    }

    private User getOrSave(OAuthProvider oAuthProvider, OAuth2User oAuth2User) {
        User user = userRepository.findByOauthId(OAuthId.of(oAuth2User.oAuthId()))
                .orElseGet(() -> oAuth2User.toEntity(oAuthProvider));
        return userRepository.save(user);
    }
}
