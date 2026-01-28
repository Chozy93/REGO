package com.itwillbs.dto;

import lombok.Getter;

@Getter
public class AdminProductSummaryDTO {

    private final long totalProductCount;
    private final long lastMonthProductCount;

    public AdminProductSummaryDTO(
        long totalProductCount,
        long lastMonthProductCount
    ) {
        this.totalProductCount = totalProductCount;
        this.lastMonthProductCount = lastMonthProductCount;
    }
}
