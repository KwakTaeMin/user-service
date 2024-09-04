package com.taemin.user.service;

import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.domain.token.RefreshToken;
import com.taemin.user.domain.token.Token;
import com.taemin.user.domain.user.User;
import com.taemin.user.exception.TokenException;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.TokenRepository;
import com.taemin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.taemin.user.common.ErrorCode.TOKEN_EXPIRED;
import static com.taemin.user.common.ErrorCode.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public void deleteToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        tokenRepository.deleteByUser(user);
    }

    @Transactional
    public void saveOrUpdate(final User user, AccessToken accessToken, RefreshToken refreshToken) {
        tokenRepository.findById(user.getUserId()).ifPresentOrElse(token -> {
            token.updateToken(accessToken, refreshToken);
            tokenRepository.save(token);
        }, () -> tokenRepository.save(Token.of(user, accessToken, refreshToken)));
    }

    public Token findByAccessTokenOrThrow(AccessToken accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
            .orElseThrow(() -> new TokenException(TOKEN_EXPIRED));
    }

    @Transactional
    public void updateToken(Token token, AccessToken accessToken) {
        token.updateAccessToken(accessToken);
        tokenRepository.save(token);
    }
}
