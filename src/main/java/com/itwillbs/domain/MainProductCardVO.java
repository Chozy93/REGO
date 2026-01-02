package com.itwillbs.domain;

import lombok.Getter;

@Getter
public class MainProductCardVO {

    private final Long id;
    private final String title;
    private final int price;
    private final String img;
    private final String loc;
    private final String time;
    private final boolean reserved;

    public MainProductCardVO(Long id,
                             String title,
                             int price,
                             String img,
                             String loc,
                             String time,
                             boolean reserved) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.img = img;
        this.loc = loc;
        this.time = time;
        this.reserved = reserved;
    }
}
