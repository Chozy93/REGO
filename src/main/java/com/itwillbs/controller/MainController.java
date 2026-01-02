package com.itwillbs.controller;

import com.itwillbs.service.MainProductListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainProductListService mainProductListService;

    @GetMapping("/")
    public String main(Model model) {

        model.addAttribute("popularList", mainProductListService.getMainProductList());
        model.addAttribute("recentList", mainProductListService.getMainProductList());
        model.addAttribute("aiList", mainProductListService.getMainProductList());

        return "main/main";
    }
}
