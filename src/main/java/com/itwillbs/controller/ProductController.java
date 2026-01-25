package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.entity.User;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.service.ProductCategoryService;
import com.itwillbs.service.ProductDetailService;
import com.itwillbs.service.ProductListService;
import com.itwillbs.service.ProductReportService;
import com.itwillbs.service.ProductService;
import com.itwillbs.view.CategoryPageVO;
import com.itwillbs.view.ProductDetailPageVO;

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
    private final ProductListService productListService;
    private final ProductReportService productReportService;
    private final UserRepository userRepository;
    private final ProductCategoryService productCategoryService;

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
	    String cookieName = "viewed_product_" + id;

	    // 1️⃣ 조회수 쿠키
	    if (request.getCookies() != null) {
	        for (Cookie cookie : request.getCookies()) {
	            if (cookieName.equals(cookie.getName())) {
	                alreadyViewed = true;
	                break;
	            }
	        }
	    }

	    if (!alreadyViewed) {
	        Cookie viewCookie = new Cookie(cookieName, "true");
	        viewCookie.setPath("/");
	        viewCookie.setMaxAge(60 * 60 * 24);
	        response.addCookie(viewCookie);
	    }

	    // 2️⃣ 로그인 여부
	    User loginUser = SecurityUtil.getCurrentUser();
	    Long loginUserId = (loginUser != null) ? loginUser.getUserId() : null;

	    ProductDetailPageVO page =
	            productDetailService.getProductDetailPage(
	                    id,
	                    !alreadyViewed,
	                    loginUserId   // ✅ Long
	            );
	    
	    System.out.println(
	    		  "loginUserId=" + loginUserId +
	    		  ", sellerId=" + page.getSellerInfo().getSellerId() +
	    		  ", mine=" + page.isMine()
	    		);
 
	 // 🔥 본인 상품 여부 판단 (Controller 책임)
	    boolean isMine =
	    	    loginUserId != null
	    	    && page.getSellerInfo() != null
	    	    && loginUserId.equals(
	    	        Long.valueOf(page.getSellerInfo().getSellerId())
	    	    );

	    // 👉 PageVO에 결과만 세팅
	    page.setMine(isMine);

	    model.addAttribute("page", page);
	    return "product/detail";
	}

	// ✅ 카테고리별 상품 목록
	@GetMapping("/products")
	public String productList(
	    @RequestParam(name = "categoryId", required = false) Long categoryId,
	    Model model
	) {
	    // 1️⃣ 카테고리 페이지 데이터 (기존)
	    CategoryPageVO page =
	        productCategoryService.getCategoryPage(categoryId);

	    // 2️⃣ 🔥 전체 부모 카테고리 리스트 (추가)
	    model.addAttribute(
	        "parentCategoryList",
	        productCategoryService.getParentCategoryList()
	    );

	    // 3️⃣ 페이지 데이터
	    model.addAttribute("page", page);

	    return "product/list";
	}

}