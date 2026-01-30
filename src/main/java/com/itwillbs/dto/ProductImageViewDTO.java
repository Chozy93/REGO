package com.itwillbs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductImageViewDTO {

    /* =========================
       이미지 식별자
    ========================= */
    private Long imageId;

    /* =========================
       이미지 URL
    ========================= */
    private String imageUrl;

    /* =========================
       정렬 순서
    ========================= */
    private int sortOrder;
}