package com.itwillbs.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class AdminInquiryListPageVO {

    private List<AdminInquiryItemVO> inquiries;

    private int currentPage;
    private int totalCount;
    private int totalPages;

    public AdminInquiryListPageVO(
            List<AdminInquiryItemVO> inquiries,
            int currentPage,
            int totalCount,
            int pageSize) {

        this.inquiries = inquiries;
        this.currentPage = currentPage;
        this.totalCount = totalCount;
        this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
    }
}
