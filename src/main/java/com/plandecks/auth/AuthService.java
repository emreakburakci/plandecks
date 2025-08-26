package com.plandecks.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final LoginLogRepository loginLogRepository;


    public String register(RegisterRequest request) {

        // Email validation eklenecek

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        return jwtUtil.generateToken(user.getEmail());
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            LoginLog log = LoginLog.builder()
                    .email(user.getEmail())
                    .success(false)
                    .timestamp(new java.util.Date())
                    .description("Invalid password attempt")
                    .build();
            loginLogRepository.save(log);
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        // Login log kaydı
        LoginLog log = LoginLog.builder()
                .email(user.getEmail())
                .success(true)
                .timestamp(new java.util.Date())
                .description("Successful login")
                .build();
        loginLogRepository.save(log);

        return token;
    }

    public boolean deleteAccount(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public boolean logout(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // Logout log kaydı
            LoginLog log = LoginLog.builder()
                    .email(user.getEmail())
                    .success(true)
                    .timestamp(new java.util.Date())
                    .description("User logged out")
                    .build();
            loginLogRepository.save(log);
            return true;
        }
        return false;
    }
}