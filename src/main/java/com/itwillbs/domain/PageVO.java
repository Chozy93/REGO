package com.itwillbs.domain;


import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageVO<T> {

    /* 데이터 */
    private final List<T> items;

    /* 페이징 */
    private final int page;        // 현재 페이지 (1-base)
    private final int size;        // 페이지 사이즈
    private final long totalCount; // 전체 건수
    private final int totalPages;  // 전체 페이지 수

    /* UI 제어 */
    private final boolean hasPrev;
    private final boolean hasNext;

    public PageVO(List<T> items, int page, int size, long totalCount) {
        this.items = items != null ? items : Collections.emptyList();
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;

        this.totalPages = (int) Math.ceil((double) totalCount / size);
        this.hasPrev = page > 1;
        this.hasNext = page < totalPages;
    }
}
