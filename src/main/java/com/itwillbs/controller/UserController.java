package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 마이페이지 컨트롤러
@Controller
public class UserController {

	@GetMapping("/mypage")
	public String myPage() {
		return "user/mypage";
	}
}
