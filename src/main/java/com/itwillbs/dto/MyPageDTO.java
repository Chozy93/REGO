package com.itwillbs.dto;

import lombok.Data;

@Data
public class MyPageDTO {
    private Long userId;
    private String nickname;
    private String profileImg;
    private String createdAt;
    private String email;
    private String password;  
    private String phoneNumber; 
    private String gender;
    private String bio;     
    

    private String address;    

    // 통계 정보들
    private int sellingCount;
    private int boughtCount;
    private int favoriteCount;
	}