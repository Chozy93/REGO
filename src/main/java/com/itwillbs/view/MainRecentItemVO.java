package com.itwillbs.view;

import com.itwillbs.dto.MainRecentViewDTO;
import lombok.Getter;

@Getter
public class MainRecentItemVO {

    private final String productId;
    private final String productName;
    private final int price;
    private final String thumbnailUrl;
    private final String regionName;

    public MainRecentItemVO(MainRecentViewDTO dto) {
        this.productId = String.valueOf(dto.getProductId());
        this.productName = dto.getProductName();
        this.price = dto.getPrice();
        this.thumbnailUrl = dto.getThumbnailUrl();
        this.regionName = dto.getRegionName();
    }
}
