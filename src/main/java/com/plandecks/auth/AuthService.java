package com.plandecks.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserLogRepository userLogRepository;
    private final TokenBlacklistService tokenBlacklistService;



    public String register(RegisterRequest request) {

        //TODO Email validation eklenecek

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        UserLog log = UserLog.builder()
                .email(user.getEmail())
                .success(true)
                .timestamp(new java.util.Date())
                .description("User registered successfully")
                .Action("REGISTER")
                .build();

        userLogRepository.save(log);

        return jwtUtil.generateToken(user.getEmail());
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            UserLog log = UserLog.builder()
                    .email(user.getEmail())
                    .success(false)
                    .timestamp(new java.util.Date())
                    .description("Invalid password attempt")
                    .Action("LOGIN")
                    .build();
            userLogRepository.save(log);
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        // Login log kaydı
        UserLog log = UserLog.builder()
                .email(user.getEmail())
                .success(true)
                .timestamp(new java.util.Date())
                .description("Successful login")
                .Action("LOGIN")
                .build();
        userLogRepository.save(log);

        return token;
    }

    public boolean deleteAccount(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            UserLog log = UserLog.builder()
                    .email(user.getEmail())
                    .success(true)
                    .timestamp(new java.util.Date())
                    .description("Account deleted successfully")
                    .Action("DELETE_ACCOUNT")
                    .build();
            userLogRepository.save(log);
            return true;
        }
        return false;
    }

    public boolean logout(String token) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email);
        if (user != null) {
            // Logout log kaydı
            tokenBlacklistService.addToBlacklist(token);
            UserLog log = UserLog.builder()
                    .email(user.getEmail())
                    .success(true)
                    .timestamp(new java.util.Date())
                    .description("User logged out")
                    .Action("LOGOUT")
                    .build();
            userLogRepository.save(log);
            return true;
        }

        UserLog log = UserLog.builder()
                .email(email)
                .success(false)
                .timestamp(new java.util.Date())
                .description("Logout attempt for non-existing user")
                .Action("LOGOUT")
                .build();

        return false;
    }

    public boolean resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // Password reset log kaydı
            UserLog log = UserLog.builder()
                    .email(user.getEmail())
                    .success(true)
                    .timestamp(new java.util.Date())
                    .description("Password reset successful")
                    .Action("RESET_PASSWORD")
                    .build();
            userLogRepository.save(log);

            return true;
        }

        UserLog log = UserLog.builder()
                .email(email)
                .success(false)
                .timestamp(new java.util.Date())
                .description("Password reset attempt for non-existing user")
                .Action("RESET_PASSWORD")
                .build();
        userLogRepository.save(log);

        return false;
    }
}