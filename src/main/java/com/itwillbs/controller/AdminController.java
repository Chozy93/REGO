package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 관리자 페이지
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("activeMenu", "dashboard");
        return "admin/dashboard";
    }

    @GetMapping("/members")
    public String members(Model model) {
        model.addAttribute("activeMenu", "members");
        return "admin/members";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("activeMenu", "products");
        return "admin/products";
    }

    @GetMapping("/trades")
    public String trades(Model model) {
        model.addAttribute("activeMenu", "trades");
        return "admin/trades";
    }

    @GetMapping("/inquiries")
    public String inquiries(Model model) {
        model.addAttribute("activeMenu", "inquiries");
        return "admin/inquiries";
    }

    @GetMapping("/notices")
    public String notices(Model model) {
        model.addAttribute("activeMenu", "notices");
        return "admin/notices";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("activeMenu", "reports");
        return "admin/reports";
    }

    @GetMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("activeMenu", "statistics");
        return "admin/statistics";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("activeMenu", "settings");
        return "admin/settings";
    }
    @GetMapping("/normal-settings")
    public String normalSettings() {
        return "admin/normal-settings";
    }

    /**
     * 회원 설정
     * URL: /admin/settings/user-settings
     * VIEW: user-settings.html
     */
    @GetMapping("/user-settings")
    public String userSettings() {
        return "admin/user-settings";
    }

    /**
     * 상품 설정
     * URL: /admin/settings/product-settings
     * VIEW: product-settings.html
     */
    @GetMapping("/product-settings")
    public String productSettings() {
        return "admin/product-settings";
    }
}

