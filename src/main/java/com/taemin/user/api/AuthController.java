package com.taemin.user.api;

import com.taemin.user.dto.response.SignUpResponse;
import com.taemin.user.dto.response.LoginResponse;
import com.taemin.user.dto.request.SignUpRequest;
import com.taemin.user.service.TokenService;
import com.taemin.user.service.UserService;
import jakarta.validation.Valid;
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

    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return ResponseEntity.ok(new SignUpResponse());
    }

    @GetMapping("/login/success")
    public ResponseEntity<LoginResponse> loginSuccess(@Valid LoginResponse loginResponse) {
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/login/fail")
    public ResponseEntity<String> loginFail() {
        return ResponseEntity.ok("login fail");
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        tokenService.deleteToken(Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.noContent().build();
    }
}
