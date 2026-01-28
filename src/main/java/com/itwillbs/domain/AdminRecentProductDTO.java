package com.itwillbs.domain;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class AdminRecentProductDTO {

    private final String productName;
    private final LocalDateTime createdAt;

    public AdminRecentProductDTO(String productName, LocalDateTime createdAt) {
        this.productName = productName;
        this.createdAt = createdAt;
    }
}

