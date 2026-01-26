package com.itwillbs.view.seller;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SellerProductCardViewVO {

    private final Long id;
    private final String title;
    private final int price;

    private final String img;
    private final String loc;
    private final String time;

    private final int likeCount;
    private final boolean liked;
    
    public SellerProductCardViewVO(
            Long id,
            String title,
            int price,
            String img,
            String loc,
            String time,
            int likeCount,
            boolean liked
    ) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.img = img;
        this.loc = loc;
        this.time = time;
        this.likeCount = likeCount;
        this.liked = liked;
    }
}
