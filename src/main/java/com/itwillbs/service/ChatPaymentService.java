package com.itwillbs.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.entity.ChatRoom;
import com.itwillbs.entity.PayWallet;
import com.itwillbs.entity.Product;
import com.itwillbs.entity.ProductOrder;
import com.itwillbs.entity.User;
import com.itwillbs.entity.enumtype.DealType;
import com.itwillbs.entity.enumtype.EscrowStatus;
import com.itwillbs.repository.ChatRoomRepository;
import com.itwillbs.repository.ProductOrderRepository;
import com.itwillbs.repository.ProductRepository;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.repository.WalletRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatPaymentService {
	
	private final ProductOrderRepository orderRepository;
    private final WalletRepository walletRepository; // 사용자의 re:pay(잔액) 엔터티
    private final ProductRepository productRepository; // 추가 필요
    private final UserRepository userRepository;       // 추가 필요
    private final ChatRoomRepository chatRoomRepository; // 추가 필요

    /**
     * 1. 판매자의 결제 요청 (주문 생성)
     */
    @Transactional
    public Long createPaymentRequest(Long productId, Long sellerId, Long buyerId, Long roomId, BigDecimal amount) {
       
    	// 1. 각 ID로 실제 엔터티 객체들을 조회 (이 과정이 빠지면 타입 불일치 에러 발생)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자가 존재하지 않습니다."));
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("구매자가 존재하지 않습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
     
        
        // 2. 조회한 객체들을 정적 팩토리 메서드에 전달
        ProductOrder order = ProductOrder.createRepayOrder(
            product, 
            buyer, 
            seller, 
            chatRoom, 
            amount, 
            DealType.DIRECT // DealType Enum 확인 필수
        );
        
        return orderRepository.save(order).getOrderId();
    }
    
    /**
     * 2. 구매자의 결제 실행 (잔액 차감 및 상태 변경)
     */
    @Transactional
    public void processRepayPayment(Long orderId, Long currentUserId) {
        // A. 주문 정보 확인
        ProductOrder order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // B. 권한 및 상태 검증
        if (!order.getBuyer().getUserId().equals(currentUserId)) {
            throw new RuntimeException("본인의 주문만 결제할 수 있습니다.");
        }
        if (order.getEscrowStatus() != EscrowStatus.PENDING) {
            throw new RuntimeException("결제 가능한 상태가 아닙니다.");
        }

        // C. 지갑(잔액) 처리
        PayWallet buyerWallet = walletRepository.findByUser_UserId(order.getBuyer().getUserId())
            .orElseThrow(() -> new RuntimeException("구매자의 지갑을 찾을 수 없습니다."));

        PayWallet sellerWallet = walletRepository.findByUser_UserId(order.getSeller().getUserId())
            .orElseThrow(() -> new RuntimeException("판매자의 지갑을 찾을 수 없습니다."));
        
        

        if (buyerWallet.getBalance().compareTo(order.getTotalAmount()) < 0) {
            throw new RuntimeException("re:pay 잔액이 부족합니다.");
        }

        // D. 실제 잔액 이동
        buyerWallet.withdraw(order.getTotalAmount()); // 구매자 차감
        sellerWallet.deposit(order.getTotalAmount());  // 판매자 적립 (또는 에스크로 보관)

        // E. 주문 상태 변경 (PENDING -> HELD)
        order.markAsPaid();
    }

}
