package com.itwillbs.controller;

import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.service.ProductLikeService;
import com.itwillbs.view.ProductLikeResultVO;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductLikeController {

    private final ProductLikeService productLikeService;

    @PostMapping("/{productId}/like")
    public ProductLikeResultVO toggleLike(
    		@PathVariable("productId") Long productId
    ) {
        // ✅ 로그인 사용자 ID 조회
    	Long userId = SecurityUtil.getCurrentUserId();
    	System.out.println("🔥 LOGIN USER ID = " + userId);

        if (userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        return productLikeService.toggleLike(productId, userId);
    }
}

