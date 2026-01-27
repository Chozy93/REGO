package com.itwillbs.view;

import lombok.Getter;

@Getter
public class MainPageVO {

    // 로그인 여부 (헤더/버튼 제어용)
    private final boolean login;

    // 메인 페이지 섹션별 VO
    private final MainAiProductListVO ai;
    private final MainPopularProductListVO popular;
    private final MainRecentProductListVO recent;
    private final MainRecentViewProductListVO recentView;

    public MainPageVO(
            boolean login,
            MainAiProductListVO ai,
            MainPopularProductListVO popular,
            MainRecentProductListVO recent,
            MainRecentViewProductListVO recentView
    ) {
        this.login = login;
        this.ai = ai;
        this.popular = popular;
        this.recent = recent;
        this.recentView = recentView;
    }
}
