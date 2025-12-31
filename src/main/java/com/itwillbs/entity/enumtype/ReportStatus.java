package com.itwillbs.entity.enumtype;

public enum ReportStatus {

    PENDING("대기중"),
    DONE("처리완료");

    private final String label;

    ReportStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
