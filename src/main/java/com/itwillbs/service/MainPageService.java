package com.itwillbs.service;

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

    public MainPageVO getMainPage(String sort, Long userId) {

        MainProductSortConditionVO condition =
                new MainProductSortConditionVO(sort);

        List<MainProductCardVO> aiProducts =
                Collections.emptyList(); // 아직 미구현

        List<MainProductCardVO> popularProducts =
                mainProductListService.getPopularProducts(userId);

        List<MainProductCardVO> recentProducts =
                mainProductListService.getRecentProducts(condition, userId);

        return new MainPageVO(aiProducts, popularProducts, recentProducts);
    }
}
