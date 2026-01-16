package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.service.ProductDetailService;
import com.itwillbs.service.ProductListService;
import com.itwillbs.service.ProductService;
import com.itwillbs.view.ProductDetailPageVO;
import com.itwillbs.view.ProductListPageVO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// 상품과 관련된 모든 것(등록, 조회, 검색, 수정, 삭제)
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final ProductListService productListService; // ⭐ 이거 필수

    // TODO: 상품 등록 기능 (2차 구현)
	@GetMapping("/product/write")
	public String createProduct() {
		
		return "product/write";
	}
	
	// ✅ 상품 상세 페이지
	@GetMapping("/product/{id}")
	public String productDetail(
	        @PathVariable("id") Long id,
	        Model model,
	        HttpServletRequest request,
	        HttpServletResponse response
	) {
		boolean alreadyViewed = false;
		String cookieName = "viewed_product_" +id;
		
		// 1️⃣ 기존 쿠키 확인
		if (request.getCookies() !=null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookieName.equals(cookie.getName())) {
					alreadyViewed = true;
					break;
				}
			}
		}
		
		// 2️⃣ 처음 보는 경우만 조회수 증가
		if (!alreadyViewed) {
	        Cookie viewCookie = new Cookie(cookieName, "true");
	        viewCookie.setPath("/");              // 전체 경로 유효
	        viewCookie.setMaxAge(60 * 60 * 24); // ⏰ 24시간
	        response.addCookie(viewCookie);
	    }
		
		// 3️⃣ 서비스 호출 (조회수 증가 여부는 Service에서 분기)
	    ProductDetailPageVO page =
	            productDetailService.getProductDetailPage(id, !alreadyViewed);

	    model.addAttribute("page", page);
	    model.addAttribute("product", page.getProduct()); // ⭐ 핵심

	    return "product/detail";
	}
	
	// ✅ 카테고리별 상품 목록
	@GetMapping("/products")
    public String productList(
            @RequestParam("categoryId") Long categoryId,
            Model model
    ) {
        ProductListPageVO page =
                productListService.getProductsByCategory(categoryId);

        model.addAttribute("page", page);
        return "product/list";
    }
}