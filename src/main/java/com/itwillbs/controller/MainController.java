package com.itwillbs.controller;

import com.itwillbs.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainPageService mainPageService;

    @GetMapping("/")
    public String main(
            @RequestParam(name = "sort", required = false, defaultValue = "recent") String sort,
            @RequestParam(name = "recentIds", required = false) String recentIds,
            @RequestParam(name = "region", required = false) String region, // 지역 파라미터 추가
            Model model
    ) {
        model.addAttribute(
                "page",
                mainPageService.getMainPage(sort, recentIds, region) // region 전달
        );

        model.addAttribute("sort", sort);
        model.addAttribute("currentRegion", region); // UI에서 표시하기 위해 추가

        return "main/main";
    }
    
}

