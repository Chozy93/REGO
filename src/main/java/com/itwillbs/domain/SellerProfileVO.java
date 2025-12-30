package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.SellerProfile;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SellerProfileVO {

    private final Long sellerId;

    private final String description;

    private final double ratingAvg;
    private final int ratingCount;
    private final int totalSales;
    private final int totalReviews;

    private final String sellerStatus;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /* =========================
       Entity → VO 생성자
    ========================= */
    public SellerProfileVO(SellerProfile entity) {
        this.sellerId = entity.getSellerId();
        this.description = entity.getDescription();
        this.ratingAvg = entity.getRatingAvg();
        this.ratingCount = entity.getRatingCount();
        this.totalSales = entity.getTotalSales();
        this.totalReviews = entity.getTotalReviews();
        this.sellerStatus = entity.getSellerStatus();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
