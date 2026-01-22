package com.itwillbs.entity.enumtype;

import lombok.Getter;

@Getter
public enum TradeStatus {
	PENDING("대기중"),
    SUCCESS("완료"), 
    FAILED("실패"),
    CANCELLED("취소");

    private final String description;

    TradeStatus(String description) {
        this.description = description;
    }
}
