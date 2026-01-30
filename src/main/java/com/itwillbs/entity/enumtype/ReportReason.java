package com.itwillbs.entity.enumtype;

public enum ReportReason {
    SPAM("스팸/홍보"),
    INAPPROPRIATE("부적절한 게시물"),
    ABUSE("욕설 및 비방"),
    FRAUD("허위 매물"),
    OTHER("기타 신고");

    private final String label;

    ReportReason(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
