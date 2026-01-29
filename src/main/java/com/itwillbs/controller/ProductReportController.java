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
import com.itwillbs.view.condition.ReportConditionVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductReportController {

    private final ProductReportService productReportService;

    @PostMapping("/productReport")
    public ApiResponse<Void> reportProduct(
            @RequestBody ReportConditionVO reportConditionVO
    ) {
        try {
            productReportService.reportProduct(reportConditionVO);

            // 성공: data 없음, success=true
            return ApiResponse.success(null);

        } catch (IllegalArgumentException e) {
            // 입력값/검증 실패
            return ApiResponse.fail(
                "REPORT_INVALID",
                e.getMessage()
            );

        } catch (Exception e) {
            // 서버 내부 오류
            return ApiResponse.fail(
                "REPORT_ERROR",
                "신고 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
            );
        }
    }



}
