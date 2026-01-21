package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
public class SellerController {

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
		 public String sellerRegister() {
		     return "seller/register";
		 }
	
	 /* =========================
	    	판매 상품 등록 페이지
	 	========================= */
		 @GetMapping("/product/register")
		 public String productRegister() {
		     return "seller/product-register";
		 }
}
