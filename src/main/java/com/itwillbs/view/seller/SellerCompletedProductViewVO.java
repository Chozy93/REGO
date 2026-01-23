package com.itwillbs.view.seller;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SellerCompletedProductViewVO {

    private final Long productId;
    private final String title;
    private final int price;
    private final String thumbnailUrl;

    public SellerCompletedProductViewVO(
            Long productId,
            String title,
            int price,
            String thumbnailUrl
    ) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
    }
}