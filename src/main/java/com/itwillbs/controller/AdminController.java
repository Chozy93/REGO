package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 관리자 페이지
@Controller
@RequestMapping("/admin")
public class AdminController {

	// 관리자 대시보드 페이지
	@GetMapping("/dashboard")
	public String dashboard() {
		return "admin/dashboard";
	}
	
}
