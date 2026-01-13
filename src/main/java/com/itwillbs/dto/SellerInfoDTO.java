package com.itwillbs.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class SellerInfoDTO {

    private Long sellerId;
    private String nickname;
    private LocalDateTime joinedAt;

    private int sellingCount;
    private int soldCount;


}
