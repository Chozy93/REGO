package com.itwillbs.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.domain.FaqVO;
import com.itwillbs.domain.NoticeVO;
import com.itwillbs.security.CustomUserDetails;
import com.itwillbs.service.CustomerService;
import com.itwillbs.view.condition.InquiryCreateConditionVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CustomerController {


	private final CustomerService customerService;

	
	// -------------- 공지사항  ---------------------
	/**
     * 공지사항 목록 조회
     */
    @GetMapping("/customer/notice")
    public String getNoticeList(Model model, 
                                @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        // 1. 상단 고정글 리스트
        List<NoticeVO> pinnedList = customerService.getPinnedNotices();
        
        // 2. 일반 공지사항 페이징 리스트
        Page<NoticeVO> noticePage = customerService.getNoticeList(pageable);
        
        model.addAttribute("pinnedList", pinnedList);
        model.addAttribute("noticeList", noticePage.getContent()); // 실제 목록 데이터
        model.addAttribute("page", noticePage); // 페이지네이션 정보
        
        return "customer/notice"; // 목록 페이지 HTML 경로
    }

	
	/**
     * 공지사항 상세페이지 조회 (isActive==1인 것들만)
     */
    @GetMapping("/customer/notice-detail")
    public String getNoticeDetail(@RequestParam("id") Long id, Model model) {
        NoticeVO notice = customerService.getNoticeDetail(id);
        model.addAttribute("notice", notice);
        System.out.println("공지사항 데이터"+notice);
        return "customer/notice-detail"; // 상세 페이지 HTML 경로
    }
	
    
	/**
     * 공지사항 작성페이지 (user ROLE == "ADMIN"인 경우만 작성 버튼 보임)
     */
	@GetMapping("/customer/notice-write")
	public String getNoticeWrite() {
		return "customer/notice-write";
	}
	
	
	
	@PostMapping("/customer/notice/write")
	@PreAuthorize("hasRole('ADMIN')") // 관리자만 접근 가능
	public String registerNotice(NoticeVO noticeVO, Authentication authentication) {
	    // 1. 현재 로그인한 관리자 정보 가져오기
	    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
	    Long writerId = user.getUserId();

	    // 2. 서비스 호출하여 저장
	    customerService.register(writerId, noticeVO);

	    return "redirect:/customer/notice"; // 등록 후 리스트로 이동
	}
	



	
	// ---------------------------- 자주 묻는 질문 -------------------------------
	/**
     * faq 리스트 조회
     */
	@GetMapping("/customer/faq")
	public String getFaqList(Model model) {
	    List<FaqVO> faqList = customerService.getActiveFaqList();
	    model.addAttribute("faqList", faqList);
	    return "customer/faq"; // FAQ HTML 경로
	}
	
	
	

	
	
	// 안전 거래 가이드
	@GetMapping("/customer/safe-guide")
	public String getSafeGuide() {
		return "customer/safe-guide";
	}

	// 1:1 문의
		@GetMapping("/customer/inquiry")
	public String inquiryPage() {
		
		return "customer/inquiry";
	}
	
	// 이용약관
		@GetMapping("/customer/terms")
		public String terms() {
			return "customer/terms";
		}
		
		// 개인정보처리방침
	    @GetMapping("/customer/privacy")
	    public String privacy() {
	        return "customer/privacy";
	    }
	    
	   // 위치기반 서비스 이용약관
	    @GetMapping("/customer/location")
	    public String location() {
	        return "customer/location";
	    }
	    
	   //  청소년 보호정책
	    @GetMapping("/customer/youth")
	    public String youth() {
	        return "customer/youth";
	    }
	    
	    @PostMapping("/inquiries")
	    public String createInquiry(
	            InquiryCreateConditionVO conditionVO
	    ) {
	    	customerService.inquiriesRegister(conditionVO);
	        return "redirect:/mypage/inquiries";
	    }
}
