package com.itwillbs.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.domain.AdminDashboardPageVO;
import com.itwillbs.domain.AdminInquirySearchConditionVO;
import com.itwillbs.domain.AdminMemberListPageVO;
import com.itwillbs.domain.AdminMemberSummaryVO;
import com.itwillbs.domain.AdminProductListPageVO;
import com.itwillbs.domain.AdminProductSearchConditionVO;
import com.itwillbs.domain.AdminProductSummaryVO;
import com.itwillbs.domain.AdminReportSummaryVO;
import com.itwillbs.dto.AdminOrderSummaryDTO;
import com.itwillbs.entity.Notice;
import com.itwillbs.service.AdminInquiryService;
import com.itwillbs.service.AdminMemberDashboardService;
import com.itwillbs.service.AdminMemberService;
import com.itwillbs.service.AdminProductDashboardService;
import com.itwillbs.service.AdminProductService;
import com.itwillbs.service.AdminReportDashboardService;
import com.itwillbs.service.AdminService;
import com.itwillbs.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminInquiryService adminInquiryService;

    private final AdminMemberDashboardService memberService;
    private final AdminProductDashboardService productService;
    private final AdminReportDashboardService reportService;
    private final AdminMemberService adminMemberService; // ✅ 추가
    private final AdminProductService adminProductService;
    private final OrderService orderService;
    private final AdminService adminService;




    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
    	
    	// 최근 주문 내역 가졍괴
    	List<AdminOrderSummaryDTO> recentOrders = orderService.getRecentOrders();

        AdminMemberSummaryVO memberSummary =
            memberService.getMemberSummary();

        AdminProductSummaryVO productSummary =
            productService.getProductSummary();

        AdminReportSummaryVO reportSummary =
            reportService.getRecentReports();
        model.addAttribute("recentOrders", recentOrders);
        
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
    public String inquiryAnswer(@PathVariable("id") Long id, Model model) {

    	// 상세 정보 조회
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
            @PathVariable("id") Long id,
            @RequestParam("answerContent") String answerContent,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
    	
    	// 로그인이 안 되어 있다면 userDetails는 null이 됩니다.
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        adminInquiryService.answerInquiry(id, answerContent, userDetails.getUsername());
        return "redirect:/admin/inquiries";
    }


    
    @GetMapping("/admin/notices")
    public String notices(Model model, 
            @PageableDefault(size = 10) Pageable pageable) {
    	// 1. 상단 요약 정보 (전체/노출/비노출)
        model.addAttribute("stats", adminService.getNoticeStats());
        
        // 2. 공지사항 리스트 데이터
        Page<Notice> noticePage = adminService.getAdminNoticeList(pageable);
        model.addAttribute("notices", noticePage);
        return "admin/notices";
    }
    
    
    
    
    // 공지사항 is_active 설정
    
    @PostMapping("/admin/notice/toggle-status")
    @ResponseBody // 페이지 이동이 아닌 데이터만 응답
    public ResponseEntity<String> toggleStatus(@RequestParam("id") Long id, @RequestParam("status") boolean status) {
        try {
            adminService.updateStatus(id, status);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail");
        }
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
