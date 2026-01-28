package com.itwillbs.gpt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GptPriceRecommendRequestDTO {

    private String title;              // 상품 제목
    private String description;        // 상세 설명
    private String conditionStatus;    // NEW / LIKE_NEW / GOOD / FAIR
    private String categoryName;       // ex) 노트북 > 울트라북
}
