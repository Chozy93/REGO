package com.itwillbs.entity.enumtype;

public enum InquiryStatus {

    PENDING("대기중"),
    ANSWERED("답변완료"),
    CANCELED("취소됨");

    private final String label;

    InquiryStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
