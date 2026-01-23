package com.itwillbs.view.seller;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SellerProfilePageViewVO {

    private final SellerProfileViewVO profile;

    private final List<SellerSellingProductViewVO> sellingProducts;
    private final List<SellerCompletedProductViewVO> completedProducts;

    private final List<SellerReviewViewVO> reviews;

    public SellerProfilePageViewVO(
            SellerProfileViewVO profile,
            List<SellerSellingProductViewVO> sellingProducts,
            List<SellerCompletedProductViewVO> completedProducts,
            List<SellerReviewViewVO> reviews
    ) {
        this.profile = profile;
        this.sellingProducts = sellingProducts;
        this.completedProducts = completedProducts;
        this.reviews = reviews;
    }
}
