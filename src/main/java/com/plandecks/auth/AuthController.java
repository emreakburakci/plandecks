package com.plandecks.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


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
        // TODO email verification to be added
        boolean deleted = authService.deleteAccount(email);
        if (deleted) {
            return "Account deleted successfully";
        } else {
            return "Account deletion failed";
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
            return "Logout successful";
        }
        return "No token provided";
    }

    //TODO reset password endpoint to be improved with email verification
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        boolean reset = authService.resetPassword(email, newPassword);
        if (reset) {
            return "Password reset successful";
        } else {
            return "Password reset failed";
        }
    }


    // test endpoint to verify if the user is authenticated
    @GetMapping("/test")
    public String testAuth() {
        return "You are authenticated";
    }
}