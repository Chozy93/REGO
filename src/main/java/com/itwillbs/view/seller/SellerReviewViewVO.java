package com.itwillbs.view.seller;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class SellerReviewViewVO {

    private Long reviewId;

    private  String buyerNickname;
    private  String buyerProfileImg;

    private  Long productId;
    private  String productTitle;
    private  String productThumbnailImg;

    private  int rating;
    private  String content;
    private  LocalDateTime  createdAt;

}
