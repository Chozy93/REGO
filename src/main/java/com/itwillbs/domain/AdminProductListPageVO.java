package com.itwillbs.domain;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import com.itwillbs.dto.AdminProductListDTO;

@Getter
public class AdminProductListPageVO {

    private final List<AdminProductListVO> products;

    private final int page;
    private final int pageSize;
    private final int totalCount;
    private final int totalPages;

    // 상태별 카운트
    private final int onSaleCount;
    private final int reservedCount;
    private final int soldCount;

    public AdminProductListPageVO(
            List<AdminProductListDTO> dtoList,
            int totalCount,
            int page,
            int pageSize,
            int onSaleCount,
            int reservedCount,
            int soldCount
    ) {
        this.products = dtoList.stream()
                .map(AdminProductListVO::new)
                .collect(Collectors.toList());

        this.totalCount = totalCount;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalCount / pageSize);

        this.onSaleCount = onSaleCount;
        this.reservedCount = reservedCount;
        this.soldCount = soldCount;
    }
}
