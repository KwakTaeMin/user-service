package com.taemin.user.filter;


import com.taemin.user.common.TokenKey;
import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.service.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
        @Nullable HttpServletRequest request,
        @Nullable HttpServletResponse response,
        @Nullable FilterChain filterChain
    ) throws ServletException, IOException {
        if (Objects.nonNull(request) && Objects.nonNull(response) && Objects.nonNull(filterChain)) {
            String accessToken = resolveToken(request);
            if (tokenProvider.validateToken(accessToken)) {
                setAuthentication(AccessToken.of(accessToken));
            } else {
                // 만료되었을 경우 accessToken 재발급
                AccessToken reissueAccessToken = tokenProvider.reissueAccessToken(AccessToken.of(accessToken));
                if (reissueAccessToken != null && StringUtils.hasText(reissueAccessToken.getAccessToken())) {
                    setAuthentication(reissueAccessToken);
                    // 재발급된 accessToken 다시 전달
                    response.setHeader(AUTHORIZATION, TokenKey.TOKEN_PREFIX + reissueAccessToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    private void setAuthentication(AccessToken accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith(TokenKey.TOKEN_PREFIX)) {
            return null;
        }
        return token.substring(TokenKey.TOKEN_PREFIX.length());
    }
}