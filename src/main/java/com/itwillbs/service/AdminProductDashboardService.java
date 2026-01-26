package com.itwillbs.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.AdminProductSummaryVO;
import com.itwillbs.dto.AdminProductSummaryDTO;
import com.itwillbs.entity.Product;
import com.itwillbs.repository.ProductRepository;

@Service
public class AdminProductDashboardService {

    private final ProductRepository productRepository;

    public AdminProductDashboardService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public AdminProductSummaryVO getProductSummary() {

        LocalDate today = LocalDate.now();

        // 총 상품 수
        long totalCount = productRepository.count();

        // 지난달 기준
        LocalDateTime lastMonthStart =
            today.minusMonths(1).atStartOfDay();
        long lastMonthCount =
            productRepository.countByCreatedAtBefore(lastMonthStart);

        AdminProductSummaryDTO summaryDTO =
            new AdminProductSummaryDTO(totalCount, lastMonthCount);

        // 최근 상품 3개
        List<Product> recentProducts =
            productRepository.findTop3ByOrderByCreatedAtDesc();

        return new AdminProductSummaryVO(summaryDTO, recentProducts);
    }
}
