package com.itwillbs.view.seller;

import lombok.Getter;
import lombok.ToString;
@Getter
@ToString
public class SellerReviewViewVO {

    private final Long reviewId;

    private final String buyerNickname;
    private final String buyerProfileImg;

    private final Long productId;
    private final String productTitle;
    private final String productThumbnailImg;

    private final int rating;
    private final String content;
    private final String createdAt;

    public SellerReviewViewVO(
            Long reviewId,
            String buyerNickname,
            String buyerProfileImg,
            Long productId,
            String productTitle,
            String productThumbnailImg,
            int rating,
            String content,
            String createdAt
    ) {
        this.reviewId = reviewId;
        this.buyerNickname = buyerNickname;
        this.buyerProfileImg = buyerProfileImg;
        this.productId = productId;
        this.productTitle = productTitle;
        this.productThumbnailImg = productThumbnailImg;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
    }
}
