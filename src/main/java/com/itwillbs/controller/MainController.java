package com.itwillbs.controller;

import com.itwillbs.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainPageService mainPageService;

    @GetMapping("/")
    public String main(Model model) {

        model.addAttribute("page",
                mainPageService.getMainPage());

        return "main/main";
    }
}
