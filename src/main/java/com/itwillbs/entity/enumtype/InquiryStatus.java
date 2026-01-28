package com.itwillbs.entity.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryStatus {

    PENDING("접수됨"),
    ANSWERED("답변 완료"),
    CANCELED("취소됨");

    private final String label;
}