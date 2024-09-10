package com.taemin.user.api;

import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.dto.request.LoginRequest;
import com.taemin.user.dto.response.LoginResponse;
import com.taemin.user.service.TokenService;
import com.taemin.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        AccessToken accessToken = userService.login(loginRequest);
        return ResponseEntity.ok(new LoginResponse(accessToken.getAccessToken()));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        tokenService.deleteToken(Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.noContent().build();
    }
}
