package com.itwillbs.view;

import com.itwillbs.view.ProductSellerInfoVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailPageVO {

	private boolean mine;
	
    private final ProductDetailVO product;
    private final ProductSellerInfoVO sellerInfo;
    private final ProductSimilarListVO similar;
    private final boolean isLogin;
    private final boolean alreadyReported;
    
    /* ===== DETAIL01_STATUS ===== */
    private final boolean isSold;
    private final boolean isReserved;


    public ProductDetailPageVO(
            ProductDetailVO product,
            ProductSellerInfoVO sellerInfo,
            ProductSimilarListVO similar,
            boolean isLogin,
            boolean alreadyReported
    ) {
        this.product = product;
        this.sellerInfo = sellerInfo;
        this.similar = similar;
        this.isLogin = isLogin;
        this.alreadyReported = alreadyReported;
        
        /* DETAIL01_STATUS */
        String statusCode = product.getSalesStatusCode();
        this.isSold = "SOLD".equals(statusCode);
        this.isReserved = "RESERVED".equals(statusCode);
    }
}
