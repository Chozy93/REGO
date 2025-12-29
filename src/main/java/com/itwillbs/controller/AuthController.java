package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 회원가입, 로그인 페이지
@Controller
public class AuthController {

	// 로그인 페이지
	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}
	
	// 회원가입 페이지
	@GetMapping("/signup")
	public String signupPage() {
		return "auth/signup";
	}
}
