package com.itwillbs.view;

import lombok.Getter;

@Getter
public class ProductLikeResultVO {
	
	private final String productId;
	private final int likeCount;
	private final boolean liked;
	
	public ProductLikeResultVO(String productId, int likeCount, boolean liked) {
		this.productId = productId;
		this.likeCount = likeCount;
		this.liked = liked;
	}

}
