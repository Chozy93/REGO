package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.itwillbs.service.SellerShopService;
import com.itwillbs.view.SellerShopPageVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SellerShopController {
	
	private final SellerShopService sellerShopService;
	
	@GetMapping("/seller/{sellerId}")
	public String sellerShop(
	        @PathVariable("sellerId") Long sellerId,
	        Model model
	) {
	    SellerShopPageVO page =
	            sellerShopService.getSellerShopPage(sellerId);

	    model.addAttribute("page", page);
	    return "seller/shop";
	}

}
