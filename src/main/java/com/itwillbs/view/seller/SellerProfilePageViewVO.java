package com.itwillbs.view.seller;

import java.util.List;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class SellerProfilePageViewVO {

    private final SellerProfileViewVO profile;

    private final List<SellerProductCardViewVO> sellingProducts;
    private final List<SellerProductCardViewVO> completedProducts;

    private final List<SellerReviewViewVO> reviews;

    public SellerProfilePageViewVO(
            SellerProfileViewVO profile,
            List<SellerProductCardViewVO> sellingProducts,
            List<SellerProductCardViewVO> completedProducts,
            List<SellerReviewViewVO> reviews
    ) {
        this.profile = profile;
        this.sellingProducts = sellingProducts;
        this.completedProducts = completedProducts;
        this.reviews = reviews;
    }
}
