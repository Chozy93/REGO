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
            @RequestParam(name = "sort", required = false, defaultValue = "recent")
            String sort,
            Model model
    ) {
        model.addAttribute(
                "page",
                mainPageService.getMainPage(sort)
        );

        // ✅ 이 한 줄이 핵심
        model.addAttribute("sort", sort);

        return "main/main";
    }

}
