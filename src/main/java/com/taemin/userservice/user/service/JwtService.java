package com.taemin.userservice.user.service;

import com.taemin.userservice.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final static String SECRET_KEY = "secretKey";
    private final static String TOKEN_TYPE = "access_token";
    private final static long EXPIRATION_TIME = 360000;
    private final static SecretKey KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    public static String generateToken(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
                .claim("userId", user.getUserId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("token-type", TOKEN_TYPE)
                .issuedAt(now)
                .expiration(new Date(nowMillis + EXPIRATION_TIME))
                .signWith(KEY, Jwts.SIG.HS512)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.get("userId", String.class);
            String name = claims.get("name", String.class);
            String email = claims.get("email", String.class);
            System.out.println("userId: " + userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
