package com.plandecks.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;


    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        String token = authService.register(request);
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return response;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return response;
    }

    @DeleteMapping
    public String deleteAccount(@RequestParam String email) {
        boolean deleted = authService.deleteAccount(email);
        if (deleted) {
            return "Account deleted successfully";
        } else {
            return "Account deletion failed";
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.addToBlacklist(token);
            return "Logout successful";
        }
        return "No token provided";
    }
}