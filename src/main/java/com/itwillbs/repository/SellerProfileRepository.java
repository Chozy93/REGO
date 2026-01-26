package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.SellerProfile;

public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {
	
	 boolean existsBySellerId(Long userId);
	 
	 boolean existsBySeller_UserId(Long userId);
}
