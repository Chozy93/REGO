package com.itwillbs.view;

import lombok.Getter;

@Getter
public class SellerShopProductCardVO {

    private final String productId;
    private final String title;
    private final int price;
    private final String thumbnailUrl;
    private final String regionName;
    private final String createdTime;
    private final int likeCount;
    private final int viewCount;

    public SellerShopProductCardVO(
            Long productId,
            String title,
            int price,
            String thumbnailUrl,
            String regionName,
            String createdTime,
            Integer likeCount,
            Integer viewCount
    ) {
        this.productId = productId != null ? productId.toString() : "";
        this.title = title != null ? title : "";
        this.price = price;
        this.thumbnailUrl = thumbnailUrl != null ? thumbnailUrl : "";
        this.regionName = regionName != null ? regionName : "";
        this.createdTime = createdTime != null ? createdTime : "";
        this.likeCount = likeCount != null ? likeCount : 0;
        this.viewCount = viewCount != null ? viewCount : 0;
    }
}
