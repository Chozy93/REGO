package com.itwillbs.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.common.response.ApiResponse;
import com.itwillbs.domain.ReportVO;
import com.itwillbs.dto.ProductReportRequest;
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

    @PostMapping("/{productId}/report")
    public ApiResponse<Void> reportProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductReportRequest request
    ) {
        productReportService.reportProduct(
            productId,
            request.getReasonCode()
        );
        return ApiResponse.success(null);
    }


}
