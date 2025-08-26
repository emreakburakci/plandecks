package com.plandecks.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtUtil jwtUtil;

    public void addToBlacklist(String token) {
        if (!blacklistedTokenRepository.existsByToken(token)) {

            Date expiration = jwtUtil.extractExpiration(token);

            BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                    .token(token)
                    .blacklistedAt(new Date())
                    .expiresAt(expiration)
                    .build();
            blacklistedTokenRepository.save(blacklistedToken);
        }
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

    public void deleteExpiredTokens() {
        Date now = new Date();
        blacklistedTokenRepository.deleteByExpiresAtBefore(now);
    }
}