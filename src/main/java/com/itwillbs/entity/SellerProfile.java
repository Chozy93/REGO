package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.SellerProfileVO;
import com.itwillbs.entity.enumtype.SellerStatus;

@Entity
@Table(name = "seller_profile")
@Getter
public class SellerProfile {

    /* =========================
       PK = FK (users.user_id)
    ========================= */
    @Id
    @Column(name = "seller_id")
    private Long sellerId;

    /* =========================
       사용자 (1:1)
    ========================= */
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "seller_id",
        foreignKey = @ForeignKey(name = "fk_seller_profile_user")
    )
    private User seller;

    /* =========================
       판매자 소개
    ========================= */
    @Column(name = "description")
    private String description;

    /* =========================
       평점 / 통계 (캐싱)
    ========================= */
    @Column(name = "rating_avg", nullable = false)
    private double ratingAvg;

    @Column(name = "rating_count", nullable = false)
    private int ratingCount;

    @Column(name = "total_sales", nullable = false)
    private int totalSales;

    @Column(name = "total_reviews", nullable = false)
    private int totalReviews;

    /* =========================
       판매자 상태
    ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "seller_status", nullable = false)
    private SellerStatus sellerStatus; // ACTIVE / SUSPENDED

    /* =========================
       약관 동의
    ========================= */
    @Column(name = "terms_agreed_at", nullable = false, updatable = false)
    private LocalDateTime termsAgreedAt;

    /* =========================
       날짜
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected SellerProfile() {}

    /* =========================
       생성자 (Domain VO → Entity)
    ========================= */
    public SellerProfile(User seller, SellerProfileVO vo) {
        this.seller = seller;

        this.description = vo.getDescription();

        /* 초기 통계값 */
        this.ratingAvg = 0.0;
        this.ratingCount = 0;
        this.totalSales = 0;
        this.totalReviews = 0;

        /* 기본 상태는 Entity 내부에서 결정 */
        this.sellerStatus = SellerStatus.ACTIVE;

        /* 약관 동의 시점 (등록 시점 기준) */
        this.termsAgreedAt = LocalDateTime.now();

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       상태 변경 / 통계 갱신
    ========================= */
    public void updateRating(double avg, int count) {
        this.ratingAvg = avg;
        this.ratingCount = count;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseSales() {
        this.totalSales++;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseReviews() {
        this.totalReviews++;
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       판매 상태 변경 (행위 메서드)
    ========================= */
    public void suspend() {
        this.sellerStatus = SellerStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.sellerStatus = SellerStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
}
