package com.itwillbs.view.condition;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewConditionVO {

    /* =========================
       Target
    ========================= */

    /** 리뷰 대상 상품 */
    private Long productId;

    /** 리뷰 대상 판매자 */
    private Long sellerId;

    /* =========================
       Review Content
    ========================= */

    /** 평점 (1 ~ 10) */
    private Integer rating;

    /** 리뷰 내용 */
    private String content;
}
