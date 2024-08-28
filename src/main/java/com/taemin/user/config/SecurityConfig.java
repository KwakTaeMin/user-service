package com.taemin.user.config;

import com.taemin.user.filter.TokenAuthenticationFilter;
import com.taemin.user.filter.TokenExceptionFilter;
import com.taemin.user.handler.CustomAccessDeniedHandler;
import com.taemin.user.handler.CustomAuthenticationEntryPoint;
import com.taemin.user.handler.OAuthFailureHandler;
import com.taemin.user.handler.OAuthSuccessHandler;
import com.taemin.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final TokenExceptionFilter tokenExceptionFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final OAuthFailureHandler oAuthFailureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable) // cors 비활성화 -> 프론트와 연결 시 따로 설정 필요
            .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
            .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
            .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
            .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable()) // X-Frame-Options 비활성화
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // session 미사용
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/error",
                    "/oauth2/**",
                    "/auth/login/success",
                    "/auth/login/fail",
                    "/auth/logout",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/favicon.ico").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> // oauth2 설정 -> OAuth2 로그인 기능에 대한 여러 설정의 진입점
                             // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
                             oauth.userInfoEndpoint(c -> c.userService(oAuth2UserService))
                                 .successHandler(oAuthSuccessHandler)
                                 .failureHandler(oAuthFailureHandler)
            )
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(tokenExceptionFilter, tokenAuthenticationFilter.getClass())
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler));

        return http.build();
    }
}
