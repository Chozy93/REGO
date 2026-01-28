package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.dto.AdminProductListDTO;

import lombok.Getter;

@Getter
public class AdminProductListVO {

    private Long productId;
    private String productName;   // ✅ 필수
    private int price;

    private String salesStatus;
    private String tradeType;

    private int viewCount;
    private int likeCount;

    private LocalDateTime createdAt;

    private Long sellerId;
    private String mainImageUrl;

    public AdminProductListVO(AdminProductListDTO dto) {
        this.productId = dto.getProductId();
        this.productName = dto.getProductName(); // ✅ 여기 중요
        this.price = dto.getPrice();

        this.salesStatus = dto.getSalesStatus();
        this.tradeType = dto.getTradeType();

        this.viewCount = dto.getViewCount();
        this.likeCount = dto.getLikeCount();

        this.createdAt = dto.getCreatedAt();
        this.sellerId = dto.getSellerId();
        this.mainImageUrl = dto.getMainImageUrl();
    }
}
