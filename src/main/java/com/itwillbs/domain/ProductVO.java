package com.itwillbs.domain;

import java.time.format.DateTimeFormatter;

import com.itwillbs.entity.Product;

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

    /* enum 제거 */
    private final String conditionStatusCode;
    private final String conditionStatusLabel;

    private final String salesStatusCode;
    private final String salesStatusLabel;

    private final String regionDisplayName;
    private final String regionCode;

    private final int viewCount;
    private final int likeCount;

    private final String tradeTypeCode;
    private final String tradeTypeLabel;

    private final String mainImageUrl;

    private final String createdAt;
    private final String updatedAt;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

        this.conditionStatusCode = entity.getConditionStatus().name();
        this.conditionStatusLabel = entity.getConditionStatus().getLabel();

        this.salesStatusCode = entity.getSalesStatus().name();
        this.salesStatusLabel = entity.getSalesStatus().getLabel();

        this.regionDisplayName = entity.getRegionDisplayName();
        this.regionCode = entity.getRegionCode();

        this.viewCount = entity.getViewCount();
        this.likeCount = entity.getLikeCount();

        this.tradeTypeCode = entity.getTradeType().name();
        this.tradeTypeLabel = entity.getTradeType().getLabel();

        this.mainImageUrl = entity.getMainImageUrl();

        this.createdAt = entity.getCreatedAt().format(FORMATTER);
        this.updatedAt = entity.getUpdatedAt() != null
                ? entity.getUpdatedAt().format(FORMATTER)
                : "";
    }
}
