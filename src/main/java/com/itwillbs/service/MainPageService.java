package com.itwillbs.service;

import com.itwillbs.security.util.SecurityUtil;

import com.itwillbs.view.MainAiProductListVO;
import com.itwillbs.view.MainPageVO;
import com.itwillbs.view.MainPopularProductListVO;
import com.itwillbs.view.MainProductCardVO;
import com.itwillbs.view.MainRecentProductListVO;
import com.itwillbs.view.MainRecentViewProductListVO;
import com.itwillbs.view.condition.MainProductSortConditionVO;

import com.itwillbs.ai.AIRecommendProductService;
import com.itwillbs.ai.AIRecommendProductVO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MainProductListService mainProductListService;
    private final MainRecentViewService mainRecentViewService;
    private final AIRecommendProductService aiRecommendProductService;

    public MainPageVO getMainPage(String sort, String recentIds, String region) {

        Long userId = SecurityUtil.getCurrentUserId();
        boolean isLogin = SecurityUtil.isAuthenticated();

        MainProductSortConditionVO condition =
                new MainProductSortConditionVO(sort);

        // ✅ AI 추천 상품 (MainProductCardVO로 받는다)
        List<MainProductCardVO> aiProducts =
                aiRecommendProductService.getRecommend(recentIds);

        List<MainProductCardVO> popularProducts =
                mainProductListService.getPopularProducts(userId, sort, region);

        List<MainProductCardVO> recentProducts =
                mainProductListService.getRecentProducts(userId, condition, region);

        List<MainProductCardVO> recentView =
                mainRecentViewService.getRecentView(recentIds);

        return new MainPageVO(
                isLogin,
                new MainAiProductListVO(aiProducts),
                new MainPopularProductListVO(popularProducts),
                new MainRecentProductListVO(recentProducts),
                new MainRecentViewProductListVO(recentView)
        );
    }
}
