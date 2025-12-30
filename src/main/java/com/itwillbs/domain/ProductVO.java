package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.Product;
import com.itwillbs.entity.enumtype.ProductConditionStatus;
import com.itwillbs.entity.enumtype.ProductSalesStatus;
import com.itwillbs.entity.enumtype.TradeType;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductVO {

    private final Long productId;
    private final Long sellerId;
    private final Long categoryId;

    private final String productName;
    private final String description;
    private final int price;

    private final ProductConditionStatus conditionStatus;
    private final ProductSalesStatus salesStatus;

    private final String regionDisplayName;
    private final String regionCode;

    private final int viewCount;
    private final int likeCount;

    private final TradeType tradeType;
    private final String mainImageUrl;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /* =========================
       Entity → VO 생성자
    ========================= */
    public ProductVO(Product entity) {
        this.productId = entity.getProductId();
        this.sellerId = entity.getSeller().getUserId();
        this.categoryId = entity.getCategory().getCategoryId();
        this.productName = entity.getProductName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.conditionStatus = entity.getConditionStatus();
        this.salesStatus = entity.getSalesStatus();
        this.regionDisplayName = entity.getRegionDisplayName();
        this.regionCode = entity.getRegionCode();
        this.viewCount = entity.getViewCount();
        this.likeCount = entity.getLikeCount();
        this.tradeType = entity.getTradeType();
        this.mainImageUrl = entity.getMainImageUrl();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
