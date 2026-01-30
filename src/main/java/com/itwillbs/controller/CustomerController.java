package com.itwillbs.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.domain.FaqVO;
import com.itwillbs.domain.NoticeVO;
import com.itwillbs.dto.InquiryRequestDTO;
import com.itwillbs.entity.Inquiry;
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
	
	
	// 공지사항 수정
	
	// 1. 수정 페이지 보기
    @GetMapping("/customer/notice-edit")
    public String editNoticePage(@RequestParam("id") Long id, Model model) {
        // ID로 기존 공지사항 정보를 가져와서 폼에 채워줌
        NoticeVO notice = customerService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "admin/notice_edit"; // 수정 폼 HTML 파일명
    }

    // 2. 수정 실행 (저장)
    @PostMapping("/customer/notice-edit")
    public String updateNotice(@ModelAttribute NoticeVO noticeVO) {
    	customerService.updateNotice(noticeVO);
        return "redirect:/admin/notices"; // 수정 후 리스트로 이동
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
	
	
	
	// ---------------------------- 1:1문의 -------------------------------
		
	// 1:1 문의 리스트 조회
	@GetMapping("/customer/inquiries")
	public String getInquiryList(
	        @AuthenticationPrincipal UserDetails userDetails,
	        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
	        @RequestParam(value = "status", required = false) String status,
	        Model model) {
	    
	    

	    String userId = userDetails.getUsername();
	    
	    // 서비스로부터 Page<InquiryRequestDTO>를 받음
	    Page<Inquiry> inquiryPage = customerService.findMyInquiries(userId, status, pageable);

	    model.addAttribute("inquiries", inquiryPage.getContent()); // 결과 리스트 (List<InquiryRequestDTO>)
	    model.addAttribute("page", inquiryPage);                   // 페이징 관련 정보 객체
	    model.addAttribute("selectedStatus", status);               // 필터 유지를 위해 선택된 타입 전달
	    
	    return "customer/inquiry-list";
	}
	
	
	
	// 1:1 문의 등록하기
	@PostMapping("/customer/inquiries") // POST 요청 처리
	public String createInquiry(InquiryRequestDTO inquiryDto,
			@AuthenticationPrincipal UserDetails userDetails) {

	    // 2. 서비스 호출 (사용자 아이디와 DTO 전달)
	     customerService.registerInquiry(inquiryDto, userDetails.getUsername());

	    // 로그로 데이터 잘 들어오는지 확인
	    System.out.println("문의 등록 요청: " + inquiryDto.toString());

	    // 3. 등록 완료 후 '문의 내역 목록' 페이지로 리다이렉트
	    return "redirect:/customer/inquiries";
	}
	
	
	// 문의 상세페이지
	@GetMapping("/customer/inquiry/detail/{id}") // URL에 문의글 번호(id)를 받도록 수정
	public String getInquiryDetail(@PathVariable("id") Long id, Model model) {
	    
	    // 1. 서비스에서 ID로 문의 내역 단건 조회
	    // .get() 대신 .orElseThrow() 등을 사용하는 것이 안전합니다.
	    Inquiry inquiry = customerService.findById(id); 
	    
	    // 2. HTML에서 'item'이라는 이름으로 쓰고 있으므로 이름을 "item"으로 지정
	    model.addAttribute("item", inquiry);
	    
	    return "customer/inquiry-detail";
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
