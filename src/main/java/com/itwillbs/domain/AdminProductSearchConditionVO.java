package com.itwillbs.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProductSearchConditionVO {

    private String keyword;
    private String salesStatus;

    // ===== 페이징 =====
    private int page = 1;        // 기본 1페이지
    private int pageSize = 10;   // 페이지당 10개

    public int getOffset() {
        return (page - 1) * pageSize;
    }
}
