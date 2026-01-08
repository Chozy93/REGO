package com.itwillbs.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductListByCategoryDTO {

    private Long productId;
    private String title;
    private int price;
    private String thumbnailUrl;
    private String regionName;
    private int likeCount;
    private boolean liked;
    private LocalDateTime createdTime;
}

