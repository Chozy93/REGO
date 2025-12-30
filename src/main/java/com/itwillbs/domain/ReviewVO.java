package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.Review;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewVO {

    private final Long reviewId;
    private final Long productId;
    private final Long buyerId;
    private final Long sellerId;

    private final String content;
    private final int rating;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /* =========================
       Entity → VO (조회)
    ========================= */
    public ReviewVO(Review entity) {
        this.reviewId = entity.getReviewId();
        this.productId = entity.getProduct().getProductId();
        this.buyerId = entity.getBuyer().getUserId();
        this.sellerId = entity.getSellerId();
        this.content = entity.getContent();
        this.rating = entity.getRating();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    /* =========================
       등록/수정용
    ========================= */
    public ReviewVO(String content, int rating) {
        this.reviewId = null;
        this.productId = null;
        this.buyerId = null;
        this.sellerId = null;
        this.content = content;
        this.rating = rating;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
