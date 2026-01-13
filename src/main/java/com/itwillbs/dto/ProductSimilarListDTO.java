package com.itwillbs.dto;

import lombok.Getter;

@Getter
public class ProductSimilarListDTO {
	
	private Long productId;
	private String title;
	private int price;
	private String thumbnailUrl;
	private String regionName;
	private int likeCount;

}
