package com.taemin.user.service;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.User;
import com.taemin.user.dto.request.UserRequest;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return getUserById(Long.valueOf(userId));
    }

    @Transactional
    public AccessToken login(UserRequest userRequest) {
        OAuthId oAuthId = OAuthId.of(userRequest.oauthId());
        User user = userRepository.findByOauthId(oAuthId).orElseGet(() -> userRepository.save(userRequest.toUser()));
        AccessToken accessToken = tokenProvider.generateToken(user);
        tokenProvider.refreshToken(user, accessToken);
        return accessToken;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

}
