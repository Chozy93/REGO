package com.itwillbs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.dto.ReviewDTO;
import com.itwillbs.security.CustomUserDetails;
import com.itwillbs.service.ReviewService;
import com.itwillbs.view.condition.ReviewConditionVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> writeReview(
            @RequestBody ReviewConditionVO reviewConditionVO
    ) {
        reviewService.writeReview(reviewConditionVO);
        return ResponseEntity.ok().build();
    }
    
    
    @GetMapping("/{productId}")
    public ResponseEntity<ReviewDTO> getReview(
    		@PathVariable("productId") Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ReviewDTO review = reviewService.getReviewByProductAndBuyer(productId, userDetails.getUserId());
        return ResponseEntity.ok(review);
    }
    
}
