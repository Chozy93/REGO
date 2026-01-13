package com.itwillbs.view;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class SellerShopPageVO {

    private final String sellerId;
    private final List<SellerShopProductCardVO> products;

    public SellerShopPageVO(
            String sellerId,
            List<SellerShopProductCardVO> products
    ) {
        this.sellerId = sellerId != null ? sellerId : "";
        this.products = products != null ? products : Collections.emptyList();
    }

    public static SellerShopPageVO empty(Long sellerId) {
        return new SellerShopPageVO(
                sellerId != null ? sellerId.toString() : "",
                Collections.emptyList()
        );
    }
}
