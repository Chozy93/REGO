package com.itwillbs.dto;

import lombok.Data;

// 사용자가 입력한 값 가져오는 용도
@Data
public class OrderRequestDTO {
	private Long orderId;
	private Long productId;
    private Long sellerId;
    private String receiverName;
    private String receiverPhone;
    private String dealType; // 'DIRECT' 또는 'DELIVERY'
    private String zipCode;
    private String address;
    private String addressDetail;
    private String deliveryRequest;       // 선택박스 값
    private String deliveryRequestDirect; // 직접입력 값
    private Long totalAmount;
    private String paymentType;           // REPAY or CARD
    private String impUid; // 아임포트 결제 고유 번호
    private boolean sandbox;
    
    // 배송 메시지 최종 결정 로직
    public String getFinalDeliveryMessage() {
        if ("직접 입력".equals(this.deliveryRequest)) {
            return this.deliveryRequestDirect;
        }
        return this.deliveryRequest;
    }
}
