package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.domain.AdminDashboardPageVO;
import com.itwillbs.domain.AdminMemberSummaryVO;
import com.itwillbs.domain.AdminProductSummaryVO;
import com.itwillbs.domain.AdminReportSummaryVO;
import com.itwillbs.service.AdminMemberDashboardService;
import com.itwillbs.service.AdminProductDashboardService;
import com.itwillbs.service.AdminReportDashboardService;

@Controller
public class AdminController {

    private final AdminMemberDashboardService memberService;
    private final AdminProductDashboardService productService;
    private final AdminReportDashboardService reportService;

    public AdminController(
        AdminMemberDashboardService memberService,
        AdminProductDashboardService productService,
        AdminReportDashboardService reportService
    ) {
        this.memberService = memberService;
        this.productService = productService;
        this.reportService = reportService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        AdminMemberSummaryVO memberSummary =
            memberService.getMemberSummary();

        AdminProductSummaryVO productSummary =
            productService.getProductSummary();

        AdminReportSummaryVO reportSummary =
            reportService.getRecentReports();

        model.addAttribute(
            "page",
            new AdminDashboardPageVO(
                memberSummary,
                productSummary,
                reportSummary
            )
        );

        return "admin/dashboard";
    }







    @GetMapping("admin/members")
    public String membersPage(Model model) {
        return "admin/members";
    }

    @GetMapping("/admin/products")
    public String products(Model model) {
        model.addAttribute("activeMenu", "products");
        return "admin/products";
    }

    @GetMapping("admin/trades")
    public String trades(Model model) {
        model.addAttribute("activeMenu", "trades");
        return "admin/trades";
    }

    @GetMapping("admin/inquiries")
    public String inquiries(Model model) {
        model.addAttribute("activeMenu", "inquiries");
        return "admin/inquiries";
    }

    @GetMapping("admin/notices")
    public String notices(Model model) {
        model.addAttribute("activeMenu", "notices");
        return "admin/notices";
    }

    @GetMapping("admin/reports")
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

