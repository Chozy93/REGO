package com.itwillbs.domain;

public class AdminInquiryDetailPageVO {

    private final AdminInquiryDetailVO inquiry;

    public AdminInquiryDetailPageVO(AdminInquiryDetailVO inquiry) {
        this.inquiry = inquiry;
    }

    public AdminInquiryDetailVO getInquiry() {
        return inquiry;
    }
}
