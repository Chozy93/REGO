package com.itwillbs.gpt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GptPriceRecommendResponseDTO {

    private int minPrice;
    private int maxPrice;
    private String reason;
}