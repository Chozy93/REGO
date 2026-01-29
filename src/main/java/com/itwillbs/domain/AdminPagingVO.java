package com.itwillbs.domain;

public class AdminPagingVO {

    private int page;
    private int size;
    private int totalPages;
    private long totalCount;

    public long getTotalCount() {
        return totalCount;
    }

    // 나머지 getter/setter 생략
}
