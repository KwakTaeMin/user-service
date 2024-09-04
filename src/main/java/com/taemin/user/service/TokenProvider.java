package com.taemin.user.service;

import com.taemin.user.common.PrincipalDetails;
import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.domain.token.RefreshToken;
import com.taemin.user.domain.token.Token;
import com.taemin.user.domain.user.User;
import com.taemin.user.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import static com.taemin.user.common.ErrorCode.INVALID_JWT_SIGNATURE;
import static com.taemin.user.common.ErrorCode.INVALID_TOKEN;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.key}")
    private String key;
    private SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;
    private static final String KEY_ROLE = "role";
    private final TokenService tokenService;
    private final EntityManager entityManager;

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public AccessToken generateToken(Authentication authentication) {
        return AccessToken.of(generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME));
    }

    @Transactional
    public void refreshToken(Authentication authentication, AccessToken accessToken) {
        RefreshToken refreshToken = RefreshToken.of(generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME));
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = entityManager.merge(principalDetails.user());
        tokenService.saveOrUpdate(user, accessToken, refreshToken);
    }

    private String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining());

        return Jwts.builder()
            .subject(authentication.getName())
            .claim(KEY_ROLE, authorities)
            .issuedAt(now)
            .expiration(expiredDate)
            .signWith(secretKey, Jwts.SIG.HS512)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(
            claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
            claims.get(KEY_ROLE).toString()));
    }

    public AccessToken reissueAccessToken(AccessToken accessToken) {
        if (StringUtils.hasText(accessToken.getAccessToken())) {
            Token token = tokenService.findByAccessTokenOrThrow(accessToken);
            RefreshToken refreshToken = token.getRefreshToken();

            if (validateToken(refreshToken.getRefreshToken())) {
                AccessToken reissueAccessToken = generateToken(getAuthentication(refreshToken.getRefreshToken()));
                tokenService.updateToken(token, reissueAccessToken);
                return reissueAccessToken;
            }
        }
        return null;
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
