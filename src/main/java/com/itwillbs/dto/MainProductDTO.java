package com.itwillbs.dto;

import lombok.Data;

@Data
public class MainProductDTO {
    private Long productId;
    private String productName;
    private int price;
    private String mainImageUrl;
    private String regionDisplayName;
    private int likeCount;
    private int viewCount;
}
