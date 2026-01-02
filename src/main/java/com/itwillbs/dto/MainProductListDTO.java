package com.itwillbs.dto;

import lombok.Getter;

@Getter
public class MainProductListDTO {

    private final Long productId;
    private final String productName;
    private final int price;
    private final String regionDisplayName;
    private final String mainImageUrl;

    public MainProductListDTO(Long productId,
                              String productName,
                              int price,
                              String regionDisplayName,
                              String mainImageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.regionDisplayName = regionDisplayName;
        this.mainImageUrl = mainImageUrl;
    }
}
