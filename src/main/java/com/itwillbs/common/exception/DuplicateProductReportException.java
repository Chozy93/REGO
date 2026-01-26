package com.itwillbs.common.exception;

public class DuplicateProductReportException extends RuntimeException {

    public DuplicateProductReportException() {
        super("이미 신고한 상품입니다.");
    }
}
