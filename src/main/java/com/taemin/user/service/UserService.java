package com.taemin.user.service;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.User;
import com.taemin.user.dto.request.SignUpRequest;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public void signUp(SignUpRequest signUpRequest) {
        OAuthId oAuthId = OAuthId.of(signUpRequest.oauthId());
        User user = userRepository.findByOauthId(oAuthId).orElseGet(() -> userRepository.save(signUpRequest.toUser()));
        //return tokenProvider.generateToken()
    }
}
