package com.taemin.user.service;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.domain.token.RefreshToken;
import com.taemin.user.domain.token.Token;
import com.taemin.user.domain.user.User;
import com.taemin.user.exception.TokenException;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import static com.taemin.user.common.ErrorCode.INVALID_JWT_SIGNATURE;
import static com.taemin.user.common.ErrorCode.INVALID_TOKEN;
import static com.taemin.user.common.ErrorCode.TOKEN_EXPIRED;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final UserRepository userRepository;
    @Value("${jwt.key}")
    private String key;
    private SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;
    private static final String KEY_ROLE = "role";
    private final TokenService tokenService;

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public AccessToken generateToken(User user) {
        return AccessToken.of(generateToken(user, ACCESS_TOKEN_EXPIRE_TIME));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void refreshToken(User user, AccessToken accessToken) {
        RefreshToken refreshToken = RefreshToken.of(generateToken(user, REFRESH_TOKEN_EXPIRE_TIME));
        tokenService.saveOrUpdate(user, accessToken, refreshToken);
    }

    private String generateToken(User user, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        String authorities = user.getRole().getAuthority();
        return Jwts.builder()
            .subject(String.valueOf(user.getUserId()))
            .claim(KEY_ROLE, authorities)
            .issuedAt(now)
            .expiration(expiredDate)
            .signWith(secretKey, Jwts.SIG.HS512)
            .compact();
    }

    public User getUserByToken(String token) {
        Claims claims = parseClaims(token);
        return userRepository.findById(Long.parseLong(claims.getSubject())).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public AccessToken reissueAccessToken(AccessToken accessToken) {
        if (StringUtils.hasText(accessToken.getAccessToken())) {
            Token token = tokenService.findByAccessTokenOrThrow(accessToken);
            RefreshToken refreshToken = token.getRefreshToken();

            if (validateToken(refreshToken.getRefreshToken())) {
                AccessToken reissueAccessToken = generateToken(getUserByToken(refreshToken.getRefreshToken()));
                tokenService.updateToken(token, reissueAccessToken);
                return reissueAccessToken;
            }
        }
        throw new TokenException(TOKEN_EXPIRED);
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return claims.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (MalformedJwtException e) {
            throw new TokenException(INVALID_TOKEN);
        } catch (SecurityException e) {
            throw new TokenException(INVALID_JWT_SIGNATURE);
        }
    }
}
