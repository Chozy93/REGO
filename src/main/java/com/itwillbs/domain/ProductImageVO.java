package com.itwillbs.domain;

import com.itwillbs.entity.ProductImage;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductImageVO {

    private final Long imageId;
    private final Long productId;
    private final String imageUrl;
    private final int sortOrder;

    /* =========================
       Entity → VO 생성자
    ========================= */
    public ProductImageVO(ProductImage entity) {
        this.imageId = entity.getImageId();
        this.productId = entity.getProduct().getProductId();
        this.imageUrl = entity.getImageUrl();
        this.sortOrder = entity.getSortOrder();
    }
}
