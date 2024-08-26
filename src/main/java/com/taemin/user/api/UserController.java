package com.taemin.user.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/current")
    public String getCurrentUser() {
        logger.info("getCurrentUser");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.info("No user is currently logged in.");
            return "No user is currently logged in.";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            logger.info("Currently logged in user: {}", userDetails.getUsername());
            return "Currently logged in user: " + userDetails.getUsername();
        } else {
            logger.info("Currently logged in user: {}", principal.toString());
            return "Currently logged in user: " + principal.toString();
        }
    }
}

