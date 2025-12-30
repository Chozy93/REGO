package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.SellerProfileVO;

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
    @Column(name = "seller_status", length = 20, nullable = false)
    private String sellerStatus; // ACTIVE / SUSPENDED

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
       생성자 (VO → Entity)
    ========================= */
    public SellerProfile(User seller, SellerProfileVO vo) {
        this.seller = seller;
        this.sellerId = seller.getUserId();
        this.description = vo.getDescription();
        this.ratingAvg = vo.getRatingAvg();
        this.ratingCount = vo.getRatingCount();
        this.totalSales = vo.getTotalSales();
        this.totalReviews = vo.getTotalReviews();
        this.sellerStatus = vo.getSellerStatus();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public SellerProfileVO toVO() {
        return new SellerProfileVO(this);
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

    public void changeStatus(String status) {
        this.sellerStatus = status;
        this.updatedAt = LocalDateTime.now();
    }
}
