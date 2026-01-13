package com.itwillbs.dto;

import lombok.Getter;

@Getter
public class MainRecentViewDTO {

    private Long productId;
    private String productName;
    private int price;
    private String thumbnailUrl;
    private String regionName;
}
