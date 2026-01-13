package com.itwillbs.view;

import com.itwillbs.dto.ProductSimilarListDTO;

import lombok.Getter;

@Getter
public class ProductSimilarItemVO {

    private final String productId;
    private final String title;
    private final int price;
    private final String thumbnailUrl;
    private final String regionName;
    private final int likeCount;

    public ProductSimilarItemVO(ProductSimilarListDTO dto) {
        this.productId = String.valueOf(dto.getProductId());
        this.title = dto.getTitle();
        this.price = dto.getPrice();
        this.thumbnailUrl = dto.getThumbnailUrl();
        this.regionName = dto.getRegionName();
        this.likeCount = dto.getLikeCount();
    }
}

