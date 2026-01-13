package com.itwillbs.view;

import lombok.Getter;

@Getter
public class SellerInfoVO {

    private final String sellerId;     // ✅ 상점 이동용
    private final String nickname;
    private final String sellerBadge;  // "신규 판매자" / "일반 판매자"
    private final int sellingCount;
    private final int soldCount;

    public SellerInfoVO(
            Long sellerId,
            String nickname,
            String sellerBadge,
            Integer sellingCount,
            Integer soldCount
    ) {
        this.sellerId = sellerId != null ? sellerId.toString() : "";
        this.nickname = nickname != null ? nickname : "";
        this.sellerBadge = sellerBadge != null ? sellerBadge : "일반 판매자";
        this.sellingCount = sellingCount != null ? sellingCount : 0;
        this.soldCount = soldCount != null ? soldCount : 0;
    }
}
