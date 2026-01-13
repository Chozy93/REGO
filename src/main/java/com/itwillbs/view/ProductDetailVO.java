package com.itwillbs.view;

import java.util.List;
import com.itwillbs.view.SellerInfoVO;


public class ProductDetailVO {

    /* ===== 상품 기본 ===== */
    private final String productId;
    private final String productName;
    private final int price;
    private final String priceDisplay;
    private final String description;

    /* ===== 이미지 ===== */
    private final List<String> imageUrls;

    /* ===== 판매자 ===== */
    private final String sellerNickname;
    private final String sellerRegion;
    private final String sellerTemperature;
    
    /* ===== 판매자 카드 (상세용) ===== */
    private SellerInfoVO seller;


    /* ===== 상태 / 통계 ===== */
    private final String conditionLabel;
    private final int viewCount;
    private int likeCount;          // ⭐ 변경 가능
    private final int chatCount;
    private boolean liked;          // ⭐ 변경 가능

    /* ===== UI 표시용 ===== */
    private final String createdAtDisplay;

    public ProductDetailVO(
            String productId,
            String productName,
            int price,
            String priceDisplay,
            String description,
            List<String> imageUrls,
            String sellerNickname,
            String sellerRegion,
            String sellerTemperature,
            String conditionLabel,
            int viewCount,
            int likeCount,
            int chatCount,
            boolean liked,
            String createdAtDisplay
    ) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.priceDisplay = priceDisplay;
        this.description = description;
        this.imageUrls = imageUrls;
        this.sellerNickname = sellerNickname;
        this.sellerRegion = sellerRegion;
        this.sellerTemperature = sellerTemperature;
        this.conditionLabel = conditionLabel;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.chatCount = chatCount;
        this.liked = liked;
        this.createdAtDisplay = createdAtDisplay;
    }

    /* ===== Getter (Thymeleaf 필수) ===== */
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getPrice() { return price; }
    public String getPriceDisplay() { return priceDisplay; }
    public String getDescription() { return description; }
    public List<String> getImageUrls() { return imageUrls; }

    public String getSellerNickname() { return sellerNickname; }
    public String getSellerRegion() { return sellerRegion; }
    public String getSellerTemperature() { return sellerTemperature; }

    public String getConditionLabel() { return conditionLabel; }
    public int getViewCount() { return viewCount; }
    public int getLikeCount() { return likeCount; }
    public int getChatCount() { return chatCount; }
    public boolean isLiked() { return liked; }

    public String getCreatedAtDisplay() { return createdAtDisplay; }

    /* ===== Setter (찜 동기화용) ===== */
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    
    /* ===== Seller 정보 (View 조합용) ===== */
    public void setSeller(SellerInfoVO seller) {
        this.seller = seller;
    }

    public SellerInfoVO getSeller() {
        return seller;
    }

}
