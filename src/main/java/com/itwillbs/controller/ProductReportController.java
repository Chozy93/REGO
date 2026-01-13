package com.itwillbs.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.common.response.ApiResponse;
import com.itwillbs.domain.ReportVO;
import com.itwillbs.entity.User;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.service.ProductReportService;
import com.itwillbs.view.ProductReportRequestVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductReportController {

    private final ProductReportService productReportService;
    private final UserRepository userRepository;

    @PostMapping("/{productId}/report")
    public ApiResponse<Void> reportProduct(
        @PathVariable("productId") Long productId,
        @RequestBody ProductReportRequestVO requestVO
    ) {
        // 🔐 임시 로그인 사용자 (DB에서 조회)
        User loginUser = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("사용자 없음"));

        // 🧾 VO 구성
        ReportVO reportVO = new ReportVO(
                "PRODUCT",
                productId,
                requestVO.getReasonCode(),
                null
        );

        // 🚀 서비스 호출
        productReportService.reportProduct(productId, reportVO, loginUser);

        return ApiResponse.success(null);
    }
}
