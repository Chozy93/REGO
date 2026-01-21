package com.itwillbs.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class SellerShopProductListDTO {

    // =========================
    // 기본 식별
    // =========================
    private Long productId;
    private Long sellerId;

    // =========================
    // 상품 정보
    // =========================
    private String productName;
    private int price;
    private String mainImageUrl;
    private String regionDisplayName;

    // =========================
    // 집계 정보
    // =========================
    private Integer likeCount;
    private Integer viewCount;

    // =========================
    // 정렬 / 표시용
    // =========================
    private LocalDateTime createdAt;
}
