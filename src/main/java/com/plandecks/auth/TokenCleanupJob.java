package com.plandecks.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenCleanupJob {

    private final TokenBlacklistService tokenBlacklistService;

    // Her gün saat 03:00'te çalışacak (CRON format: saniye dakika saat gün ay günOfWeek)
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanExpiredTokens() {
        tokenBlacklistService.deleteExpiredTokens();
        System.out.println("Expired blacklisted tokens temizlendi ✅");
    }
}