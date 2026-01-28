package com.itwillbs.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class AiRecommendProductDTO {

	private Long productId;
	private String title;
	private int price;
	private String thumbnailUrl;
	private String location;
	private LocalDateTime createdAt;
	private int likeCount;
	private boolean liked;

}
