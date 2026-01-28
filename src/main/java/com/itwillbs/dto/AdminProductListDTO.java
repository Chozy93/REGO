package com.itwillbs.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProductListDTO {

    private Long productId;
    private String productName;
    private Integer price;
    private String salesStatus;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private Long sellerId;
    private String mainImageUrl;
    private String tradeType;   // ✅ 추가
    private int likeCount;
	
}
