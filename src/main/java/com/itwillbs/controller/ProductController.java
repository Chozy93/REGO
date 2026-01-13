package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.entity.User;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.service.ProductDetailService;
import com.itwillbs.service.ProductListService;
import com.itwillbs.service.ProductReportService;
import com.itwillbs.service.ProductService;
import com.itwillbs.view.ProductDetailPageVO;
import com.itwillbs.view.ProductListPageVO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
	    boolean isLogin = false;
	    User loginUser = null;

	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        loginUser = (User) session.getAttribute("loginUser");
	        isLogin = (loginUser != null);
	    }

	    // 🔥 테스트 중이면 임시 로그인
	     isLogin = true;
	     loginUser = userRepository.findById(1L).orElse(null);

	    // 3️⃣ PageVO 생성
	    ProductDetailPageVO page =
	            productDetailService.getProductDetailPage(id, !alreadyViewed, isLogin);

	    // 4️⃣ DETAIL01_REPORT_STATUS
	    boolean alreadyReported = false;
	    if (loginUser != null) {
	        alreadyReported =
	                productReportService.isAlreadyReported(id, loginUser);
	    }

	    page.setAlreadyReported(alreadyReported);

	    model.addAttribute("page", page);
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