package com.itwillbs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchDTO {
	private String keyword;        // 상품명 검색
    private Long categoryId;       // 카테고리 필터
    private Integer minPrice;      // 최소 가격
    private Integer maxPrice;      // 최대 가격
    private String conditionStatus; // 상품 상태 (NEW, USED)
    private String regionSidoCode;  // 지역 필터
  //  private String sort = "latest"; // 정렬 (latest, priceLow, priceHigh)
}
