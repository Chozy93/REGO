package com.itwillbs.view;

import java.util.List;

import com.itwillbs.view.product.list.CategoryBarVO;

import lombok.Getter;


@Getter
public class ProductListPageVO {

    private final CategoryBarVO categoryBar;   // ⭐ LIST00 핵심
    private final List<MainProductCardVO> cards;

    public ProductListPageVO(
            CategoryBarVO categoryBar,
            List<MainProductCardVO> cards
    ) {
        this.categoryBar = categoryBar;
        this.cards = cards != null ? cards : List.of();
    }
}

