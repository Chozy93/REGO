package com.itwillbs.entity.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum PayTransactionType {
	CHARGE("충전"),
    WITHDRAW("출금"),
    PAYMENT("구매"),
    INCOME("판매 수익"),
    REFUND("환불");
	
	private final String description;

	PayTransactionType(String description) {
        this.description = description;
    }
}
