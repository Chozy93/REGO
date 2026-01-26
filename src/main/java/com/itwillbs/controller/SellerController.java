package com.itwillbs.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwillbs.dto.ProductRegionDTO;
import com.itwillbs.entity.User;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.service.SellerService;
import com.itwillbs.view.condition.SellerProductRegisterConditionVO;
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
	 	
	// 내 판매자 프로필
	 @GetMapping("/myprofile")
	 public String mySellerProfilePage(Model model) {

	     User loginUser = SecurityUtil.getCurrentUser();

	     /* 로그인 안 된 경우 (방어 코드) */
	     if (loginUser == null) {
	         return "redirect:/login";
	     }

	     /* 아직 판매자 등록 안 한 경우 → 판매자 등록 페이지 */
	     if (!sellerService.hasSellerProfile(loginUser)) {
	         return "redirect:/seller/register";
	     }

	     /* 판매자 등록 완료된 경우 → 프로필 */
	     Long sellerId = loginUser.getUserId();

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
		 /* =========================
	    	판매 상품 지역 검색 
	 	========================= */
		 
		 @GetMapping("/product/regions/search")
		 @ResponseBody
		 public List<ProductRegionDTO> searchRegions(@RequestParam("keyword") String keyword) {
			    return sellerService.searchProductRegions(keyword);
			}
		 
		 
		 
		 /* =========================
		      상품 등록 처리
		     ========================= */
		 @PostMapping("/product/register")
		 public String registerProduct(
		         @ModelAttribute SellerProductRegisterConditionVO conditionVO,
		         @RequestParam(name = "images", required = false) List<MultipartFile> images,
		         RedirectAttributes redirectAttributes
		 ) {
			 if (images != null) {
				    images = images.stream()
				        .filter(file -> !file.isEmpty())
				        .toList();

				    if (images.isEmpty()) {
				        images = null;
				    }
				}

		     Long productId = sellerService.productRegister(conditionVO, images);

		     redirectAttributes.addFlashAttribute("successMessage", "상품이 등록되었습니다.");

		     return "redirect:/product/" + productId;
		 }


}
