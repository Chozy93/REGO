package com.itwillbs.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.itwillbs.view.condition.SellerProductRegisterConditionVO;

@Getter
@AllArgsConstructor
public class SellerProductEditViewDTO {

    /* =========================
       상품 수정 폼 데이터
       - 등록/수정 공용
    ========================= */
    private SellerProductRegisterConditionVO condition;

    /* =========================
       기존 상품 이미지 목록
       - 삭제 대상 관리용
    ========================= */
    private List<ProductImageViewDTO> images;
}