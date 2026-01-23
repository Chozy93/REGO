package com.itwillbs.view;

import com.itwillbs.domain.ReviewVO;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SellerReviewViewVO {

    private final Long reviewId;

    private final String buyerNickname; // 추가
    private final int rating;
    private final String content;

    private final String createdAt;

    public SellerReviewViewVO(
            ReviewVO vo,
            String buyerNickname
    ) {
        this.reviewId = vo.getReviewId();
        this.buyerNickname = buyerNickname;
        this.rating = vo.getRating();
        this.content = vo.getContent();
        this.createdAt = vo.getCreatedAt().toString();
    }
}
