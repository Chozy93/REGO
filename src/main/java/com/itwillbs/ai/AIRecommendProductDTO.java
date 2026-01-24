package com.itwillbs.ai;

import lombok.Getter;

@Getter
public class AIRecommendProductDTO {

    private Long productId;
    private String title;
    private int price;
    private String thumbnail;

    // ✅ 추가 (기본 false)
    private boolean liked = false;
}
