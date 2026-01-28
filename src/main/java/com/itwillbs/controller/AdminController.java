package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.domain.AdminDashboardPageVO;
import com.itwillbs.domain.AdminInquiryListPageVO;
import com.itwillbs.domain.AdminInquirySearchConditionVO;
import com.itwillbs.domain.AdminMemberSummaryVO;
import com.itwillbs.domain.AdminProductListPageVO;
import com.itwillbs.domain.AdminProductSearchConditionVO;
import com.itwillbs.domain.AdminProductSummaryVO;
import com.itwillbs.domain.AdminReportSummaryVO;
import com.itwillbs.domain.AdminMemberListPageVO;
import com.itwillbs.service.AdminInquiryService;
import com.itwillbs.service.AdminMemberDashboardService;
import com.itwillbs.service.AdminProductDashboardService;
import com.itwillbs.service.AdminProductService;
import com.itwillbs.service.AdminReportDashboardService;
import com.itwillbs.service.AdminMemberService;

@Controller
public class AdminController {

    private final AdminInquiryService adminInquiryService;

    private final AdminMemberDashboardService memberService;
    private final AdminProductDashboardService productService;
    private final AdminReportDashboardService reportService;
    private final AdminMemberService adminMemberService; // ✅ 추가
    private final AdminProductService adminProductService;


    public AdminController(
    	    AdminMemberDashboardService memberService,
    	    AdminProductDashboardService productService,
    	    AdminReportDashboardService reportService,
    	    AdminMemberService adminMemberService,
    	    AdminProductService adminProductService,   // ✅ 추가
    	    AdminInquiryService adminInquiryService
    	) {
    	    this.memberService = memberService;
    	    this.productService = productService;
    	    this.reportService = reportService;
    	    this.adminMemberService = adminMemberService;
    	    this.adminProductService = adminProductService; // ✅ 초기화
    	    this.adminInquiryService = adminInquiryService;
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

    /**
     * 회원 관리 페이지
     * URL: /admin/members
     * VIEW: admin/members.html
     */
    @GetMapping("/admin/members")
    public String membersPage(Model model) {

        AdminMemberListPageVO page =
            adminMemberService.getAdminMemberList();

        model.addAttribute("page", page);
        model.addAttribute("activeMenu", "members");

        return "admin/members";
    }

    @GetMapping("/admin/products")
    public String products(
            @ModelAttribute AdminProductSearchConditionVO condition,
            Model model) {

        AdminProductListPageVO page =
                adminProductService.getAdminProductList(condition);

        model.addAttribute("page", page);
        model.addAttribute("condition", condition);

        return "admin/products";
    }





    @GetMapping("/admin/inquiries")
    public String inquiries(
            @ModelAttribute AdminInquirySearchConditionVO condition,
            Model model) {

        model.addAttribute("page", adminInquiryService.getInquiryPage(condition));
        model.addAttribute("totalCount", adminInquiryService.getTotalCount());
        model.addAttribute("waitingCount", adminInquiryService.getWaitingCount());
        model.addAttribute("doneCount", adminInquiryService.getDoneCount());
        model.addAttribute("activeMenu", "inquiries");

        return "admin/inquiries";
    }
 // 문의 상세 (답변완료)
    @GetMapping("/admin/inquiries/{id}")
    public String inquiryDetail(@PathVariable Long id, Model model) {

        model.addAttribute(
            "inquiry",
            adminInquiryService.getInquiryDetail(id)
        );
        model.addAttribute("activeMenu", "inquiries");

        return "admin/inquiry-detail";
    }

    // 문의 답변 작성 (답변대기)
    @GetMapping("/admin/inquiries/{id}/answer")
    public String inquiryAnswer(@PathVariable Long id, Model model) {

        model.addAttribute(
            "inquiry",
            adminInquiryService.getInquiryDetail(id)
        );
        model.addAttribute("activeMenu", "inquiries");

        return "admin/inquiry-answer";
    }

    // 답변 등록
    @PostMapping("/admin/inquiries/{id}/answer")
    public String submitInquiryAnswer(
            @PathVariable Long id,
            @RequestParam String answerContent
    ) {
        adminInquiryService.answerInquiry(id, answerContent);
        return "redirect:/admin/inquiries/" + id;
    }


    
    @GetMapping("/admin/notices")
    public String notices(Model model) {
        model.addAttribute("activeMenu", "notices");
        return "admin/notices";
    }
    
    @GetMapping("/admin/notice_write")
    public String notice_write() {
    	return "admin/notice_write";
    }

    @GetMapping("/admin/reports")
    public String reports(Model model) {
        model.addAttribute("activeMenu", "reports");
        return "admin/reports";
    }

    @GetMapping("admin/statistics")
    public String statistics(Model model) {
        model.addAttribute("activeMenu", "statistics");
        return "admin/statistics";
    }

    @GetMapping("admin/settings")
    public String settings(Model model) {
        model.addAttribute("activeMenu", "settings");
        return "admin/settings";
    }

    @GetMapping("admin/normal-settings")
    public String normalSettings() {
        return "admin/normal-settings";
    }

    @GetMapping("admin/user-settings")
    public String userSettings() {
        return "admin/user-settings";
    }

    @GetMapping("admin/product-settings")
    public String productSettings() {
        return "admin/product-settings";
    }
}
