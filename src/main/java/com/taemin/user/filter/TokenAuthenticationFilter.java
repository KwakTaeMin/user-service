package com.taemin.user.filter;


import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.domain.user.User;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX = "Bearer ";

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
                AccessToken reissueAccessToken = tokenProvider.reissueAccessToken(AccessToken.of(accessToken));
                if (reissueAccessToken != null && StringUtils.hasText(reissueAccessToken.getAccessToken())) {
                    setAuthentication(reissueAccessToken);
                    response.setHeader(AUTHORIZATION, TOKEN_PREFIX + reissueAccessToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    private void setAuthentication(AccessToken accessToken) {
        User user = tokenProvider.getUserByToken(accessToken.getAccessToken());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        return token.substring(TOKEN_PREFIX.length());
    }
}