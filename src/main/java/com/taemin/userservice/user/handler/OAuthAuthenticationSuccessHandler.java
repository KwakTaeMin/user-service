package com.taemin.userservice.user.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taemin.userservice.user.domain.User;
import com.taemin.userservice.user.domain.UserOAuth;
import com.taemin.userservice.user.repository.UserOAuthRepository;
import com.taemin.userservice.user.repository.UserRepository;
import com.taemin.userservice.user.service.JwtService;
import com.taemin.userservice.user.type.OAuthProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final UserOAuthRepository userOAuthRepository;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2Token.getPrincipal();

        OAuthProvider provider = getOAuthProvider(oAuth2Token);
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email).orElseGet(() -> createUser(oAuth2User));

        String accessToken = saveOrUpdateUserOAuth(user, provider, oAuth2User);
        // JSON 응답 생성
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Collections.singletonMap("token", accessToken)));
    }

    private OAuthProvider getOAuthProvider(OAuth2AuthenticationToken oAuth2Token) {
        String providerName = oAuth2Token.getAuthorizedClientRegistrationId().toUpperCase();
        return OAuthProvider.valueOf(providerName);
    }

    private User createUser(OAuth2User oAuth2User) {
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String profilePicture = oAuth2User.getAttribute("picture");
        return userRepository.save(new User(name, email, profilePicture));
    }

    private String saveOrUpdateUserOAuth(User user, OAuthProvider provider, OAuth2User oAuth2User) {
        String oauthUserId = oAuth2User.getName();

        String accessToken = jwtService.generateToken(user);

        UserOAuth userOAuth = userOAuthRepository.findByUserAndOAuthProvider(user, provider)
                .orElseGet(() -> new UserOAuth(user, provider, oauthUserId, accessToken));

        userOAuth.updateTokens(accessToken);
        userOAuthRepository.save(userOAuth);
        return accessToken;
    }
}
