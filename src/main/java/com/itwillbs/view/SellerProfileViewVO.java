package com.itwillbs.view;

import com.itwillbs.domain.SellerProfileVO;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SellerProfileViewVO {

    private final Long sellerId;

    private final String description;

    private final double ratingAvg;
    private final int ratingCount;
    private final int totalSales;
    private final int totalReviews;

    private final String sellerStatusLabel;

    public SellerProfileViewVO(SellerProfileVO vo) {
        this.sellerId = vo.getSellerId();
        this.description = vo.getDescription();
        this.ratingAvg = vo.getRatingAvg();
        this.ratingCount = vo.getRatingCount();
        this.totalSales = vo.getTotalSales();
        this.totalReviews = vo.getTotalReviews();
        this.sellerStatusLabel = vo.getSellerStatusLabel();
    }
}
