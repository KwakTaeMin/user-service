package com.taemin.user.service;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.domain.user.OAuth2User;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.User;
import com.taemin.user.dto.request.LoginRequest;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final OAuthUserService oAuthUserService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return getUserById(Long.valueOf(userId));
    }

    @Transactional
    public AccessToken login(LoginRequest loginRequest) {
        OAuth2User oAuth2User = oAuthUserService.getOAuthUser(loginRequest.toOauthToken());
        OAuthId oAuthId = OAuthId.of(oAuth2User.oAuthId());
        User user = userRepository.findByOauthId(oAuthId).orElseGet(() -> userRepository.save(oAuth2User.toEntity(loginRequest.oAuthProvider())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AccessToken accessToken = tokenProvider.generateToken(user);
        tokenProvider.refreshToken(user, accessToken);
        eventPublisher.publishEvent(new AuthenticationSuccessEvent(authentication));
        return accessToken;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }
}
