package com.itwillbs.view;

import lombok.Getter;

@Getter
public class SellerShopSellerProfileVO {

    private final String sellerId;
    private final String nickname;
    private final String regionName;
    private final int productCount;

    public SellerShopSellerProfileVO(
            Long sellerId,
            String nickname,
            String regionName,
            Integer productCount
    ) {
        this.sellerId = sellerId != null ? sellerId.toString() : "";
        this.nickname = nickname != null ? nickname : "판매자";
        this.regionName = regionName != null ? regionName : "";
        this.productCount = productCount != null ? productCount : 0;
    }
}
