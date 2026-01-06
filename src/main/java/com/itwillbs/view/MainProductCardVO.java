package com.itwillbs.view;

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
}
