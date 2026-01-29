package com.itwillbs.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.itwillbs.dto.OrderListResponseDTO;
import com.itwillbs.entity.Notice;
import com.itwillbs.entity.Report;
import com.itwillbs.entity.enumtype.UserRole;
import com.itwillbs.entity.enumtype.UserStatus;
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
    
    // user ROLE권한 부여
    @ResponseBody
    @PatchMapping("/admin/users/{userId}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable("userId") Long userId,
            @RequestParam("role") UserRole role) {
        
        try {
            adminService.updateUserRole(userId, role);
            return ResponseEntity.ok("권한이 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("변경 실패");
        }
    }
    
    // user status 상태 변경
    @ResponseBody
    @PatchMapping("/admin/users/{userId}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable("userId") Long userId,
            @RequestParam("status") UserStatus status) { // Enum으로 바로 매핑
        
        try {
            adminService.updateUserStatus(userId, status);
            return ResponseEntity.ok("상태가 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("변경 실패");
        }
    }
    
    
    
    // 상품 관리 페이지

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





    // 문의 관리 페이지
    
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

    // 신고하기 페이지
    @GetMapping("admin/reports")
    public String reportList(@RequestParam(value = "status", required = false) String status,
                             @PageableDefault(size = 10) Pageable pageable,
                             Model model) {
    	
    	// 리스트 가져오기
    	Page<Report> reportPage = adminService.getReportList(pageable);
    	// 1. 실제 데이터 리스트 (.getContent() 사용)
        model.addAttribute("reports", reportPage.getContent());
       // 2. 페이지 정보 전체 (페이지네이션용)
        model.addAttribute("reportPage", reportPage);
        model.addAttribute("stats", adminService.getReportStats());
        return "admin/reports";
    }
    
 // 처리 완료 버튼 클릭 시 신고 상태 변경하기
    @ResponseBody
    @PatchMapping("/admin/reports/{reportId}/status")
    public ResponseEntity<String> updateReportStatus(
            @PathVariable("reportId") Long reportId,
            @RequestParam("newStatus") String newStatus) {
        
        try {
            adminService.updateReportStatus(reportId, newStatus);
            return ResponseEntity.ok("상태가 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("변경 실패: " + e.getMessage());
        }
    }
    
    
    
    // -------------- 거래 관리 페이지
    @GetMapping("/admin/orders")
    public String getOrderList(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "search", required = false) String search,
            Model model) {

        // 1. 서비스 호출하여 페이징된 데이터 가져오기
        // DTO는 조회 전용으로 설계된 OrderListResponseDTO를 사용한다고 가정합니다.
        Page<OrderListResponseDTO> orderPage = adminService.findAllOrders(status, search, pageable);

        // 2. View에 데이터 전달
        model.addAttribute("orders", orderPage.getContent()); // 리스트 데이터
        model.addAttribute("page", orderPage);                // 페이징 정보
        model.addAttribute("currentStatus", status);          // 필터 유지용
        model.addAttribute("searchKeyword", search);          // 검색어 유지용

        // 3. 통계 데이터 (상단 stat-box용)
        model.addAttribute("totalCount", adminService.getTotalCount());
        model.addAttribute("disputeCount", adminService.getDisputeCount());

        return "admin/order"; // admin/order.html 반환
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
