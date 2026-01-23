package com.itwillbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.UserAddress;

public interface UserAddressReopsitory extends JpaRepository<UserAddress, Long>{
	// 특정 유저의 배송지 목록 중 '기본 배송지'인 것 하나만 조회
    // Optional을 써서 기본 배송지가 없을 경우를 대비합니다.
    Optional<UserAddress> findByUserUserIdAndIsDefaultTrue(Long userId);
}
