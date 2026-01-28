package com.itwillbs.view;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itwillbs.dto.MainProductListDTO;

import lombok.Getter;

@Getter
public class MainProductCardVO {

    private final String id;        // 🔥 Long → String
    private final String title;
    private final int price;
    private final String img;
    private final String loc;
    private final String time;
    private final boolean reserved;
    
 // 🔥 추가
    private final int likeCount;   
    private final boolean liked;

    public MainProductCardVO(
            String id,
            String title,
            int price,
            String img,
            String loc,
            String time,
            boolean reserved,
            int likeCount,
            boolean liked
    ) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.img = img;
        this.loc = loc;
        this.time = time;
        this.reserved = reserved;
        this.likeCount = likeCount;
        this.liked = liked;
    }
    
 // 🔥🔥🔥 이 생성자만 추가하면 끝
    public MainProductCardVO(MainProductListDTO dto) {
        this.id = String.valueOf(dto.getProductId());
        this.title = dto.getTitle();
        this.price = dto.getPrice();
        this.img = dto.getThumbnailUrl();      // ⚠ DTO 필드명에 맞게
        this.loc = dto.getRegionName();        // ⚠ DTO 필드명에 맞게
        this.time = dto.getTimeAgo();      // ⚠ DTO 필드명에 맞게
        this.reserved = false;     // ⚠ DTO에 없으면 false
        this.likeCount = dto.getLikeCount();
        this.liked = dto.isLiked();
    }
    
    
    

    
}

