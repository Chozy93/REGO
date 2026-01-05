package com.itwillbs.service;

import com.itwillbs.view.MainPageVO;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MainProductListService mainProductListService;

    public MainPageVO getMainPage() {

        return new MainPageVO(
                List.of(), // 🔒 AI 추천 (지금은 비워둠)
                mainProductListService.getPopularProducts(),
                mainProductListService.getRecentProducts()
        );
    }
}
