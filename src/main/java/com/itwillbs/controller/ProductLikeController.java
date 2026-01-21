package com.itwillbs.controller;

import com.itwillbs.domain.user.UserVO;
import com.itwillbs.security.CustomUserDetails;
import com.itwillbs.service.ProductLikeService;
import com.itwillbs.view.ProductLikeResultVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductLikeController {

    private final ProductLikeService productLikeService;

    @PostMapping("/{productId}/like")
    public ProductLikeResultVO toggleLike(
            @PathVariable("productId") Long productId,
            Authentication authentication
    ) {

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("🚩 로그인이 되어 있지 않습니다.");
            return null; 
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            

            Long userId = userDetails.getUser().getUserId();
            
            System.out.println("🚩 시큐리티에서 찾은 유저 ID: " + userId);
            return productLikeService.toggleLike(productId, userId);
        }

        System.out.println("🚩 유저 정보를 확인할 수 없습니다.");
        return null;
    }

}

