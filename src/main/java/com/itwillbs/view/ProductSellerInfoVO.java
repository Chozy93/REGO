package com.itwillbs.view;

import com.itwillbs.dto.ProductSellerInfoDTO;

import lombok.Getter;

@Getter
public class ProductSellerInfoVO {

    private String sellerId;
    private String nickname;
    private int productCount;
    private String ratingLabel;
    private boolean isNewSeller;

    public ProductSellerInfoVO(ProductSellerInfoDTO dto) {
        this.sellerId = String.valueOf(dto.getSellerId());
        this.nickname = dto.getNickname();
        this.productCount = dto.getProductCount() != null ? dto.getProductCount() : 0;

        if (dto.getRating() == null) {
            this.ratingLabel = "평점 없음";
        } else {
            this.ratingLabel = String.format("%.1f", dto.getRating());
        }

        this.isNewSeller = this.productCount < 3;
    }
}
