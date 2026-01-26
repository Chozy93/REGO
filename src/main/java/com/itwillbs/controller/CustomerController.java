package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
	
	@GetMapping("/inquiry")
	public String inquiryPage() {
		
		return "customer/inquiry";
	}
	
	// 이용약관
		@GetMapping("/terms")
		public String terms() {
			return "customer/terms";
		}
		
		// 개인정보처리방침
	    @GetMapping("/privacy")
	    public String privacy() {
	        return "customer/privacy";
	    }
	    
	   // 위치기반 서비스 이용약관
	    @GetMapping("/location")
	    public String location() {
	        return "customer/location";
	    }
	    
	   //  청소년 보호정책
	    @GetMapping("/youth")
	    public String youth() {
	        return "customer/youth";
	    }
	
}
