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
