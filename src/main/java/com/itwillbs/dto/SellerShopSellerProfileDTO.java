package com.itwillbs.dto;

import lombok.Getter;

@Getter
public class SellerShopSellerProfileDTO {

    private Long sellerId;
    private String nickname;
    private String regionName;   // 항상 null
    private Integer productCount;
}
