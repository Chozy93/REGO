package com.itwillbs.controller;

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
        return productLikeService.toggleLike(productId);
    }

}

