package com.itwillbs.dto;

import com.itwillbs.entity.enumtype.EscrowStatus;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderListResponseDTO {
	private Long orderId;
    private String productName;
    private String productImgUrl;
    private String categoryName;
    private String buyerName;
    private String sellerName;
    private Long totalAmount;
    private LocalDateTime createdAt;
    private String status;
    private String statusLabel;

    // JPQL 전용 생성자: 인자의 개수(9개)와 타입을 쿼리와 정확히 일치시킴
    public OrderListResponseDTO(Long orderId, String productName, String mainImageUrl, 
                                String categoryName, String buyerUsername, String sellerUsername, 
                                Object totalAmount, LocalDateTime createdAt, EscrowStatus escrowStatus) {
        this.orderId = orderId;
        this.productName = productName;
        this.productImgUrl = mainImageUrl;
        this.categoryName = categoryName;
        this.buyerName = buyerUsername;
        this.sellerName = sellerUsername;
        
        // 숫자 타입 불일치 방지 (Integer or Long -> Long)
        this.totalAmount = (totalAmount instanceof Number) ? ((Number) totalAmount).longValue() : 0L;
        
        this.createdAt = createdAt;
        this.status = (escrowStatus != null) ? escrowStatus.name() : null;
        this.statusLabel = (escrowStatus != null) ? escrowStatus.getLabel() : "";
    }
}