package com.itwillbs.view.seller;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SellerProfileViewVO {

    /* =========================
       seller
    ========================= */
    private final Long sellerId;
    private final String nickname;
    private final String profileImg;

    private final String introduction;

    private final double ratingAvg;
    private final int totalReviews;
    private final int totalSales;

    /* =========================
       생성자 (MyBatis 직행)
    ========================= */
    public SellerProfileViewVO(
            Long sellerId,
            String nickname,
            String profileImg,
            String introduction,
            double ratingAvg,
            int totalReviews,
            int totalSales
    ) {
        this.sellerId = sellerId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.introduction = introduction;
        this.ratingAvg = ratingAvg;
        this.totalReviews = totalReviews;
        this.totalSales = totalSales;
    }
}
