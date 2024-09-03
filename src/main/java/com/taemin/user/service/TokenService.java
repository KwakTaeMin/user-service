package com.taemin.user.service;

import com.taemin.user.domain.Token;
import com.taemin.user.domain.User;
import com.taemin.user.exception.TokenException;
import com.taemin.user.repository.TokenRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.taemin.user.common.ErrorCode.TOKEN_EXPIRED;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public void deleteRefreshToken(Long userId) {
        tokenRepository.deleteById(userId);
    }

    @Transactional
    public void saveOrUpdate(final User user, String accessToken, String refreshToken) {
        tokenRepository.findById(user.getUserId()).ifPresentOrElse(token -> {
            token.updateToken(accessToken, refreshToken);
            tokenRepository.save(token);
        }, () -> {
            tokenRepository.save(new Token(user, accessToken, refreshToken));
        });
    }

    public Token findByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
            .orElseThrow(() -> new TokenException(TOKEN_EXPIRED));
    }

    @Transactional
    public void updateToken(Token token, String accessToken) {
        token.updateAccessToken(accessToken);
        tokenRepository.save(token);
    }
}
