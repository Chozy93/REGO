package com.itwillbs.domain;

import java.util.List;

import com.itwillbs.dto.AdminProductSummaryDTO;
import com.itwillbs.entity.Product;

import lombok.Getter;

@Getter
public class AdminProductSummaryVO {

    private final int totalProductCount;
    private final String increasePercent;
    private final boolean isIncrease;

    private final List<AdminRecentProductItemVO> recentProducts;

    public AdminProductSummaryVO(
        AdminProductSummaryDTO dto,
        List<Product> products
    ) {
        this.totalProductCount = (int) dto.getTotalProductCount();

        PercentResult result =
            calculate(dto.getTotalProductCount(), dto.getLastMonthProductCount());

        this.increasePercent = result.percent();
        this.isIncrease = result.isIncrease();

        this.recentProducts = products.stream()
            .map(AdminRecentProductItemVO::new)
            .toList();
    }

    private PercentResult calculate(long current, long base) {
        if (base <= 0) {
            return new PercentResult("0", false);
        }
        double value = ((double)(current - base) / base) * 100;
        return new PercentResult(
            String.format("%.0f", value),
            value > 0
        );
    }

    private record PercentResult(String percent, boolean isIncrease) {}
}
