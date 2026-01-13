package com.itwillbs.view;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProductListPageVO {

    private final Long categoryId;

    // 🔥 product-card fragment와 의미 맞춤
    private final List<MainProductCardVO> cards;

    // ✅ 카테고리 이름
    @Setter
    private String categoryName;

    public ProductListPageVO(Long categoryId, List<MainProductCardVO> cards) {
        this.categoryId = categoryId;
        this.cards = cards;
    }
}
