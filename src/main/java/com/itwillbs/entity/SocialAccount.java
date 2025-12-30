package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.entity.enumtype.SocialProvider;

@Entity
@Table(
    name = "social_accounts",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_social_accounts_provider_id",
            columnNames = "provider_id"
        )
    }
)
@Getter
public class SocialAccount {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_id")
    private Long socialId;

    /* =========================
       사용자 (N:1)
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_social_accounts_user")
    )
    private User user;

    /* =========================
       소셜 제공자
    ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private SocialProvider provider;

    /* =========================
       소셜 고유 식별자
    ========================= */
    @Column(name = "provider_id", length = 50, nullable = false)
    private String providerId;

    /* =========================
       연동 일시
    ========================= */
    @Column(name = "connected_at", nullable = false)
    private LocalDateTime connectedAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected SocialAccount() {}

 
}
