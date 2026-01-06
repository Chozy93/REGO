package com.itwillbs.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ProductListByCategoryDTO {

    private Long productId;
    private String title;
    private int price;
    private String thumbnailUrl;
    private String regionName;
    private int likeCount;
    private LocalDateTime createdTime;
}

