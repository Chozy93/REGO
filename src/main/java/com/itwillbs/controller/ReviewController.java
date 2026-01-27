package com.itwillbs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
