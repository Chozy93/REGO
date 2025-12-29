package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 상품과 관련된 모든 것(등록, 조회, 검색, 수정, 삭제)
@Controller
public class ProductController {

	// 판매하기 페이지 (상품 등록)
	@GetMapping("/product/write")
	public String createProduct() {
		
		return "product/write";
	}
}
