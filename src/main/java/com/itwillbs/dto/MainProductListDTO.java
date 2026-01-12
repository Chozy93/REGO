package com.itwillbs.dto;

import lombok.Getter;

@Getter
public class MainProductListDTO {

    private final Long productId;          // 상품 ID
    private final String title;             // 상품명
    private final int price;                // 가격
    private final String thumbnailUrl;      // 썸네일 이미지
    private final String regionName;         // 지역명
    private final String createdTime;        // 등록 시간 (가공된 문자열)
    private final int likeCount;             // ❤️ 찜 개수
    private final boolean liked;             // ❤️ 찜 여부 (로그인 사용자 기준)

    public MainProductListDTO(
            Long productId,
            String title,
            int price,
            String thumbnailUrl,
            String regionName,
            String createdTime,
            int likeCount,
            boolean liked
    ) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.regionName = regionName;
        this.createdTime = createdTime;
        this.likeCount = likeCount;
        this.liked = liked;
    }
}
