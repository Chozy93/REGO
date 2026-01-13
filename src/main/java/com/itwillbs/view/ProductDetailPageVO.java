package com.itwillbs.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailPageVO {

    private final ProductDetailVO product;
    private final SellerInfoVO seller;
    private final ProductSimilarListVO similar;

    private final boolean isLogin;

    // 🔥 final 제거
    private boolean alreadyReported;

    public ProductDetailPageVO(
        ProductDetailVO product,
        SellerInfoVO seller,
        ProductSimilarListVO similar,
        boolean isLogin
    ) {
        this.product = product;
        this.seller = seller;
        this.similar = similar;
        this.isLogin = isLogin;
    }
}

