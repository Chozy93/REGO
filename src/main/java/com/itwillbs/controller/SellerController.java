package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.domain.SellerProfileVO;
import com.itwillbs.entity.User;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.service.MainPageService;
import com.itwillbs.service.SellerService;
import com.itwillbs.view.condition.SellerRegisterConditionVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
	
	 private final SellerService sellerService;
	
	   /* =========================
	    	판매자 프로필 페이지
		 ========================= */
		 @GetMapping("/profile")
		 public String sellerProfile() {
		     return "seller/profile";
		 }
	
		 /* =========================
	       판매자 등록 페이지
	    ========================= */
	    @GetMapping("/register")
	    public String registerPage() {

	        User loginUser = SecurityUtil.getCurrentUser();

	        /* 이미 판매자 프로필이 있으면 메인으로 */
	        if (sellerService.hasSellerProfile(loginUser)) {
	            return "redirect:/";
	        }

	        return "seller/register";
	    }
		 /* =========================
	       판매자 등록 처리
	    ========================= */
		 @PostMapping("/register")
		 public String registerSeller(SellerRegisterConditionVO conditionVO) {

		     User loginUser = SecurityUtil.getCurrentUser();

		     sellerService.createSellerProfile(loginUser, conditionVO);

		     return "redirect:/";
		 }
		 @GetMapping("/register/complete")
		 public String registerComplete() {
		     return "seller/register-complete";
		 }
	
	 /* =========================
	    	판매 상품 등록 페이지
	 	========================= */
		 @GetMapping("/product/register")
		 public String productRegister() {
		     return "seller/product-register";
		 }
}
