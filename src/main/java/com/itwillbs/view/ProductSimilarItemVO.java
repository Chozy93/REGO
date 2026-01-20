package com.itwillbs.view;

import com.itwillbs.dto.ProductSimilarDTO;
import lombok.Getter;

@Getter
public class ProductSimilarItemVO {

    private final String productId;
    private final String productName;
    private final int price;
    private final String thumbnailUrl;
    private final String regionName;

    public ProductSimilarItemVO(ProductSimilarDTO dto) {
        this.productId = String.valueOf(dto.getProductId());
        this.productName = dto.getProductName();
        this.price = dto.getPrice();
        this.thumbnailUrl =
                dto.getThumbnailUrl() != null ? dto.getThumbnailUrl() : "";
        this.regionName =
                dto.getRegionName() != null ? dto.getRegionName() : "";
    }
}
