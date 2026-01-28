package com.itwillbs.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long reviewId;
    private String content;
    private int rating;
    private LocalDateTime createdAt;
    
    // 상품 정보
    private Long productId;
    private String productName;
    private String productImageUrl;
    
    // 작성자(작성후기면 상대방, 받은후기면 쓴 사람) 정보
    private String writerNickname;
    private String writerProfileImg;
    
    // 구분을 위한 필드 (RECEIVED / SENT)
    private String type; 
    
    private String regionDisplayName; // 상품 거래 지역
    private String sellerNickname;    // 작성후기에서 보여줄 판매자 닉네임
    private String sellerProfileImg;
}