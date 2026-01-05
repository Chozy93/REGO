package com.itwillbs.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.dto.ProductDetailDTO;
import com.itwillbs.service.ProductDetailService;
import com.itwillbs.service.ProductService;
import com.itwillbs.view.ProductDetailPageVO;

import lombok.RequiredArgsConstructor;

// 상품과 관련된 모든 것(등록, 조회, 검색, 수정, 삭제)
@Controller
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	private final ProductDetailService productDetailService;

	// 판매하기 페이지 (상품 등록)
	@GetMapping("/product/write")
	public String createProduct() {
		
		return "product/write";
	}
	
	// ✅ 상품 상세 페이지
	@GetMapping("/product/{id}")
	public String productDetail(
	        @PathVariable("id") Long id,
	        Model model
	) {
	    ProductDetailPageVO page =
	            productDetailService.getProductDetailPage(id);

	    model.addAttribute("page", page);
	    model.addAttribute("product", page.getProduct()); // ⭐ 핵심

	    return "product/detail";
	}

}
