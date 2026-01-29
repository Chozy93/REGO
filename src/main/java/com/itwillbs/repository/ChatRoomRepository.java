package com.itwillbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.ChatRoom;
import com.itwillbs.entity.Product;
import com.itwillbs.entity.User;
import com.itwillbs.entity.enumtype.ChatRoomType;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    /* =========================
       상품 + 구매자 기준 채팅방 조회
    ========================= */
    Optional<ChatRoom> findByProductAndBuyer(Product product, User buyer);
    
    
    Optional<ChatRoom> findBySellerAndBuyerAndRoomType(
            User seller,
            User buyer,
            ChatRoomType roomType
    );

}
