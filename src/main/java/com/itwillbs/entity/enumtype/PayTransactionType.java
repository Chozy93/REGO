package com.itwillbs.entity.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayTransactionType {
	CHARGE("충전"),
    PAYMENT("결제"),
    REFUND("환불"),
    TRANSFER("송금");

    private final String description;
}
