package com.plandecks.auth;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false, unique = true)
    private String token;

    private Date blacklistedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;  // Token’ın bitiş süresi
}