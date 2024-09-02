package com.taemin.user.filter;

import com.taemin.user.exception.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TokenExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @Nullable HttpServletRequest request,
        @Nullable HttpServletResponse response,
        @Nullable FilterChain filterChain
    ) throws ServletException, IOException {
        if (Objects.nonNull(request) && Objects.nonNull(response) && Objects.nonNull(filterChain)) {
            try {
                filterChain.doFilter(request, response);
            } catch (TokenException e) {
                response.sendError(e.getErrorCode().getHttpStatus().value(), e.getMessage());
            }
        }
    }
}