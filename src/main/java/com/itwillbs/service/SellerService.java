package com.itwillbs.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.SellerProfileVO;
import com.itwillbs.entity.SellerProfile;
import com.itwillbs.entity.User;
import com.itwillbs.mapper.SellerMapper;
import com.itwillbs.repository.SellerProfileRepository;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.view.condition.SellerRegisterConditionVO;
import com.itwillbs.view.seller.ReviewFilterConditionVO;
import com.itwillbs.view.seller.SellerProfilePageViewVO;
import com.itwillbs.view.seller.SellerProfileViewVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerProfileRepository sellerProfileRepository;
    private final UserRepository userRepository;
    private final SellerMapper sellerMapper;
    /* =========================
	    판매자 프로필 존재 여부
	 ========================= */
	 @Transactional(readOnly = true)
	 public boolean hasSellerProfile(User user) {
	     return sellerProfileRepository.existsById(user.getUserId());
	 }
    
	 //판매자 프로필 생성
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
	 
	 
	 /* =========================
     판매자 프로필 페이지 조회
  ========================= */
	 @Transactional(readOnly = true)
	    public SellerProfilePageViewVO getSellerProfilePage(
	            Long sellerId,
	            ReviewFilterConditionVO conditionVO,
	            int offset,
	            int size
	    ) {
	        SellerProfileViewVO profile =
	                sellerMapper.selectSellerProfile(sellerId);

	        if (profile == null) {
	            throw new IllegalArgumentException("판매자 프로필이 존재하지 않습니다.");
	        }

	        return new SellerProfilePageViewVO(
	                profile,
	                sellerMapper.selectSellingProducts(sellerId),
	                sellerMapper.selectCompletedProducts(sellerId),
	                sellerMapper.selectSellerReviews(sellerId, conditionVO, offset, size)
	        );
	    }



}
