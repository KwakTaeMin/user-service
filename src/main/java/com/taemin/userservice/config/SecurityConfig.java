package com.taemin.userservice.config;

import com.taemin.userservice.user.handler.OAuthAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/login", "/oauth2/**").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                    //.loginPage("/login") front login page
                    .successHandler(oAuthAuthenticationSuccessHandler)
                    //.defaultSuccessUrl("/", true)
            ).logout(logout -> logout
                    .logoutSuccessUrl("/login?logout") // 로그아웃 후 리다이렉트 URL
                    .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
