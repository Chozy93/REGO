package com.itwillbs.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminInquirySearchConditionVO {

    // 페이지
    private int page = 1;
    private int size = 10;

    // 검색
    private String keyword; // 제목 검색

    // 필터
    private String status; // WAITING, DONE
    private String period; // WEEK, MONTH

    public int getOffset() {
        return (page - 1) * size;
    }
}
