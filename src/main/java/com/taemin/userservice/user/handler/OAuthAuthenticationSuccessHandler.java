package com.taemin.userservice.user.handler;

import com.taemin.userservice.user.domain.User;
import com.taemin.userservice.user.domain.UserOAuth;
import com.taemin.userservice.user.repository.UserOAuthRepository;
import com.taemin.userservice.user.repository.UserRepository;
import com.taemin.userservice.user.type.OAuthProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final UserOAuthRepository userOAuthRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2Token.getPrincipal();

        String providerName = oAuth2Token.getAuthorizedClientRegistrationId().toUpperCase();
        OAuthProvider provider = OAuthProvider.valueOf(providerName);

        // todo : provider 별 OAuth 데이터 확인 필요
        String oauthUserId = oAuth2User.getName();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String profilePicture = oAuth2User.getAttribute("picture");
        String accessToken = oAuth2User.getAttribute("access_token");
        String refreshToken = oAuth2User.getAttribute("refresh_token");
        LocalDateTime expiresIn = LocalDateTime.now().plusSeconds(oAuth2User.getAttribute("expires_in"));

        Optional<User> existingUser = userRepository.findByEmail(email);

        // todo : OAuth 계정 별로 통합이 될수 있을까
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User(name, email, profilePicture);
            userRepository.save(user);
        }

        UserOAuth userOAuth = userOAuthRepository.findByUserAndOAuthProvider(user, provider)
            .orElseGet(() ->
                           new UserOAuth(user,
                                         provider,
                                         oauthUserId,
                                         accessToken,
                                         refreshToken,
                                         expiresIn
                           )
            );

        userOAuthRepository.save(userOAuth);

        response.sendRedirect("/");
    }
}
