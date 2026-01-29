package com.itwillbs.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminProductStatusCountDTO {

    private int onSaleCount;
    private int reservedCount;
    private int soldCount;
}
