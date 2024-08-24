package com.taemin.userservice.user.service;

import com.taemin.userservice.filter.JwtAuthenticationFilter;
import com.taemin.userservice.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final static String SECRET_KEY = "secretKeysecretKeysecretKeysecretKeysecretKey";
    private final static long EXPIRATION_TIME = 360000;
    private final static SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
                .claim("userId", user.getUserId())
                .issuedAt(now)
                .expiration(new Date(nowMillis + EXPIRATION_TIME))
                .signWith(KEY, Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(KEY)
                .build();
        return (Claims) jwtParser.parse(token).getPayload();
    }

    public void validateToken(String token) {
        try {
            Claims claims = parseToken(token);

            String userId = claims.get("userId", String.class);
            logger.info("user: {}", userId);
        } catch (Exception e) {
            if (e instanceof SignatureException) {
                throw new RuntimeException("JWT Token Invalid Exception");
            } else if (e instanceof ExpiredJwtException) {
                throw new RuntimeException("JWT Token Expired Exception");
            } else {
                throw new RuntimeException("JWT Exception");
            }
        }
    }
}
