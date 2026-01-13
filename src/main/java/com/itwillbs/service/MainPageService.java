package com.itwillbs.service;

import com.itwillbs.view.MainPageVO;
import com.itwillbs.view.MainProductCardVO;
import com.itwillbs.view.MainRecentViewVO;
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

    public MainPageVO getMainPage(String sort, String recentIds) {

        MainProductSortConditionVO condition =
                new MainProductSortConditionVO(sort);

        List<MainProductCardVO> aiProducts =
                Collections.emptyList(); // 아직 미구현

        List<MainProductCardVO> popularProducts =
                mainProductListService.getPopularProducts();

        List<MainProductCardVO> recentProducts =
                mainProductListService.getRecentProducts(condition);

        // ✅ 1️⃣ 먼저 기존 방식대로 page 생성
        MainPageVO page = new MainPageVO(
                aiProducts,
                popularProducts,
                recentProducts
        );

        // ✅ 2️⃣ 여기서 recentView "조합"
       
        // 🔥 핵심: recentIds null 방어
        if (recentIds != null && !recentIds.isBlank()) {
            page.setRecentView(
                    mainRecentViewService.getRecentView(recentIds)
            );
        } else {
            page.setRecentView(
                    MainRecentViewVO.empty() // or new MainRecentViewVO(emptyList)
            );
        }
        // ✅ 3️⃣ return
        return page;
    }

}
