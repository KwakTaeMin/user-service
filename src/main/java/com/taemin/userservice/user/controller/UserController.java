package com.taemin.userservice.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/")
    public String getCurrentUser() {
        // 현재 인증된 사용자의 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "No user is currently logged in.";
        }

        // 인증된 사용자의 세부 정보 가져오기
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return "Currently logged in user: " + userDetails.getUsername();
        } else {
            return "Currently logged in user: " + principal.toString();
        }
    }
}

