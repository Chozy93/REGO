package com.itwillbs.dto;

import lombok.Getter;

@Getter
public class ProductSellerInfoDTO {

    private Long sellerId;
    private String nickname;
    private Integer productCount;
    private Double rating; // 없을 수 있음
}
