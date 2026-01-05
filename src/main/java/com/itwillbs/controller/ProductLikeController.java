package com.itwillbs.controller;

import com.itwillbs.service.ProductLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductLikeController {

    private final ProductLikeService productLikeService;

    // =========================
    // 찜 토글 (임시 더미)
    // =========================
    @PostMapping("/{productId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long productId
    ) {
        // ⚠️ 로그인 아직 안 붙음 → user 고정
        String dummyUserId = "testUser";

        Map<String, Object> result =
                productLikeService.toggleLike(productId, dummyUserId);

        return ResponseEntity.ok(result);
    }
}
