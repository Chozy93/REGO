package com.itwillbs.ai;

import lombok.Getter;

@Getter
public class AIRecommendProductVO {

    private final String id;        // 🔥 공통 카드 규격
    private final String title;
    private final int price;
    private final String thumbnail;

    private final boolean liked;
    private final int likeCount;

    public AIRecommendProductVO(AIRecommendProductDTO dto) {
        this.id = String.valueOf(dto.getProductId()); // 🔥 여기
        this.title = dto.getTitle();
        this.price = dto.getPrice();
        this.thumbnail = dto.getThumbnail();

        this.liked = false;
        this.likeCount = 0;
    }
}
