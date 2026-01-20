package com.itwillbs.view;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class SellerShopPageVO {

    private final String sellerId;
    private final SellerShopSellerProfileVO sellerProfile;
    private final List<SellerShopProductCardVO> products;

    public SellerShopPageVO(
            Long sellerId,
            SellerShopSellerProfileVO sellerProfile,
            List<SellerShopProductCardVO> products
    ) {
        this.sellerId = sellerId != null ? sellerId.toString() : "";
        this.sellerProfile = sellerProfile;
        this.products = products != null ? products : Collections.emptyList();
    }
}
