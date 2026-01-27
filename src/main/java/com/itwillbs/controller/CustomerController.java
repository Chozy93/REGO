package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {
	
	// 공지사항
	@GetMapping("/customer/notice")
	public String getNotice() {
		return "customer/notice";
	}
	
	// 공지사항 상세페이지
	@GetMapping("/customer/notice-detail")
	public String getNoticeDetail() {
		return "customer/notice-detail";
	}
	
	// 공지사항 작성페이지 (admin계정일 경우에만 )
	@GetMapping("/customer/notice-write")
	public String getNoticeWrite() {
		return "customer/notice-write";
	}
	
	// 자주 묻는 질문
	@GetMapping("/customer/faq")
	public String getFaq() {
		return "customer/faq";
	}

	// 1:1 문의
	@GetMapping("/customer/qna")
	public String getQna() {
		return "customer/qna";
	}
	
	
	// 안전 거래 가이드
	@GetMapping("/customer/safe-guide")
	public String getSafeGuide() {
		return "customer/safe-guide";
	}
}
