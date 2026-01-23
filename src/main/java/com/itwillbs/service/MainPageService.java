package com.itwillbs.service;

import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.view.MainPageVO;
import com.itwillbs.view.MainProductCardVO;
import com.itwillbs.view.condition.MainProductSortConditionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MainProductListService mainProductListService;
    private final MainRecentViewService mainRecentViewService;

    public MainPageVO getMainPage(String sort, String recentIds, String region) {

        Long userId = SecurityUtil.getCurrentUserId(); // 로그인 아니면 null

        MainProductSortConditionVO condition = new MainProductSortConditionVO(sort);

        List<MainProductCardVO> aiProducts =
                mainProductListService.getPopularProducts(userId, sort, region);

        List<MainProductCardVO> popularProducts =
                mainProductListService.getPopularProducts(userId, sort, region);

        List<MainProductCardVO> recentProducts =
                mainProductListService.getRecentProducts(userId, condition, region);

        boolean isLogin = SecurityUtil.isAuthenticated();

        List<MainProductCardVO> recentView =
        	    mainRecentViewService.getRecentView(recentIds);

        	MainPageVO page =
        	    new MainPageVO(
        	        isLogin,
        	        aiProducts,
        	        popularProducts,
        	        recentProducts, // 최근 등록
        	        recentView      // 최근 본
        	    );

        	return page;

    }
    
}