package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


// footer 하단 약관 컨트롤러
@Controller
public class PolicyController {
	
	// 이용약관
	@GetMapping("/terms")
	public String terms() {
		return "policy/terms";
	}
	
	// 개인정보처리방침
    @GetMapping("/privacy")
    public String privacy() {
        return "policy/privacy";
    }
    
   // 위치기반 서비스 이용약관
    @GetMapping("/location")
    public String location() {
        return "policy/location";
    }
    
   //  청소년 보호정책
    @GetMapping("/youth")
    public String youth() {
        return "policy/youth";
    }

}
