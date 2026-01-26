package com.itwillbs.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductCategoryItemDTO {

    private final Long productId;
    private final String title;
    private final int price;
    private final String imageUrl;
    private final String regionName;
    private final LocalDateTime createdAt;

    private final int likeCount;
    private final boolean liked;
}
