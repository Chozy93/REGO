package com.itwillbs.view;

import java.util.Collections;
import java.util.List;

public class MainPageVO {

    private final boolean login;

    private final List<MainProductCardVO> aiProducts;
    private final List<MainProductCardVO> popularProducts;
    private final List<MainProductCardVO> recentProducts; // 최근 등록
    private final List<MainProductCardVO> recentView;     // 최근 본

    public MainPageVO(
            boolean login,
            List<MainProductCardVO> aiProducts,
            List<MainProductCardVO> popularProducts,
            List<MainProductCardVO> recentProducts,
            List<MainProductCardVO> recentView
    ) {
        this.login = login;
        this.aiProducts = aiProducts != null ? aiProducts : Collections.emptyList();
        this.popularProducts = popularProducts != null ? popularProducts : Collections.emptyList();
        this.recentProducts = recentProducts != null ? recentProducts : Collections.emptyList();
        this.recentView = recentView != null ? recentView : Collections.emptyList();
    }

    public boolean isLogin() {
        return login;
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

    public List<MainProductCardVO> getRecentView() {
        return recentView;
    }
}
