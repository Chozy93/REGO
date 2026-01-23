package com.itwillbs.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.entity.User;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.service.SellerService;
import com.itwillbs.view.condition.SellerRegisterConditionVO;
import com.itwillbs.view.seller.ReviewFilterConditionVO;
import com.itwillbs.view.seller.ReviewSortType;
import com.itwillbs.view.seller.SellerProfilePageViewVO;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
	
	 private final SellerService sellerService;
	 	
	 //내 프로필
	 @GetMapping("/myprofile")
	 public String mySellerProfilePage(Model model) {

	     Long sellerId = SecurityUtil.getCurrentUserId(); // 네가 이미 쓰는 유틸

	     SellerProfilePageViewVO sellerProfilePageViewVO =
	             sellerService.getSellerProfilePage(
	                     sellerId,
	                     new ReviewFilterConditionVO(ReviewSortType.LATEST),
	                     0,
	                     5
	             );

	     model.addAttribute("sellerProfilePageViewVO", sellerProfilePageViewVO);
	     return "seller/profile";
	 }
	   /* =========================
	    	판매자 프로필 페이지
		 ========================= */
	 @GetMapping("/profile/{sellerId}")
	    public String sellerProfilePage(
	    		@PathVariable(value = "sellerId", required = true) Long sellerId,
	            Model model
	    ) {
	        ReviewFilterConditionVO conditionVO =
	                new ReviewFilterConditionVO(ReviewSortType.LATEST);

	        SellerProfilePageViewVO sellerProfilePageViewVO =
	        		sellerService.getSellerProfilePage(
	                        sellerId,
	                        conditionVO,
	                        0,      // offset
	                        5       // size
	                );

	        model.addAttribute("sellerProfilePageViewVO", sellerProfilePageViewVO);
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

		     return "seller/register-complete";
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
		 
		 @GetMapping("/entry")
		 public String sellerEntry(
		 ) {
			 User user = SecurityUtil.getCurrentUser();

		     boolean hasSellerProfile =
		         sellerService.hasSellerProfile(user);

		     if (hasSellerProfile) {
		         return "redirect:/seller/product/register";
		     } else {
		         return "redirect:/seller/register";
		     }
		 }

}
