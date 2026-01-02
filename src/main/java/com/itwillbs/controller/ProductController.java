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
import com.itwillbs.service.ProductService;

import lombok.RequiredArgsConstructor;

// 상품과 관련된 모든 것(등록, 조회, 검색, 수정, 삭제)
@Controller
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;

	// 판매하기 페이지 (상품 등록)
	@GetMapping("/product/write")
	public String createProduct() {
		
		return "product/write";
	}
	
	// ✅ 상품 상세 페이지
	@GetMapping("/product/{id}")
	public String productDetail(@PathVariable("id") Long id, Model model) {
		
		ProductDetailDTO product = productService.getProductDetail(id);
		model.addAttribute("product", product);
		
		return "product/detail";
		
	}
	
	// ✅ 찜 토글 (Ajax)
	@PostMapping("/product/{productId}/like")
    @ResponseBody
    public Map<String, Object> toggleLike(@PathVariable Long productId) {

        return productService.toggleLike(productId);
    }
}
