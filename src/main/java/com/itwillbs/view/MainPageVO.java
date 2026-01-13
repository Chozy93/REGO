package com.itwillbs.view;

import java.util.Collections;
import java.util.List;

public class MainPageVO {

    private final List<MainProductCardVO> aiProducts;
    private final List<MainProductCardVO> popularProducts;
    private final List<MainProductCardVO> recentProducts;

    public MainPageVO(
            List<MainProductCardVO> aiProducts,
            List<MainProductCardVO> popularProducts,
            List<MainProductCardVO> recentProducts
    ) {
        this.aiProducts = aiProducts != null
                ? aiProducts
                : Collections.emptyList();

        this.popularProducts = popularProducts != null
                ? popularProducts
                : Collections.emptyList();

        this.recentProducts = recentProducts != null
                ? recentProducts
                : Collections.emptyList();
    }

    public List<MainProductCardVO> getAiProducts() {
        return aiProducts;
    }

    public List<MainProductCardVO> getPopularProducts() {
        return popularProducts;
    }

    public List<MainProductCardVO> getRecentProducts() {
        return recentProducts;
    }
}
