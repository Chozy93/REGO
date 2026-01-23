package com.itwillbs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.SellerProfileVO;
import com.itwillbs.entity.SellerProfile;
import com.itwillbs.entity.User;
import com.itwillbs.repository.SellerProfileRepository;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.view.condition.SellerRegisterConditionVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerProfileRepository sellerProfileRepository;
    private final UserRepository userRepository;
    
    /* =========================
	    판매자 프로필 존재 여부
	 ========================= */
	 @Transactional(readOnly = true)
	 public boolean hasSellerProfile(User user) {
	     return sellerProfileRepository.existsById(user.getUserId());
	 }
    
    
	 @Transactional
	 public void createSellerProfile(User user,
	                                 SellerRegisterConditionVO conditionVO) {

	     Long userId = user.getUserId();

	     if (sellerProfileRepository.existsBySeller_UserId(userId)) {
	         throw new IllegalStateException("이미 판매자 프로필이 존재합니다.");
	     }

	     // 🔑 FK 용도 → 프록시가 베스트
	     User managedUser = userRepository.getReferenceById(userId);

	     SellerProfileVO sellerProfileVO =
	             new SellerProfileVO(conditionVO);

	     SellerProfile sellerProfile =
	             new SellerProfile(managedUser, sellerProfileVO);

	     sellerProfileRepository.save(sellerProfile);
	 }


}
