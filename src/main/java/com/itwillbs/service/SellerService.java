package com.itwillbs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.SellerProfileVO;
import com.itwillbs.entity.SellerProfile;
import com.itwillbs.entity.User;
import com.itwillbs.repository.SellerProfileRepository;
import com.itwillbs.view.condition.SellerRegisterConditionVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerProfileRepository sellerProfileRepository;
    
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

        if (sellerProfileRepository.existsById(user.getUserId())) {
            throw new IllegalStateException("이미 판매자 프로필이 존재합니다.");
        }

        /* Condition VO → Domain VO (Service 책임) */
        SellerProfileVO sellerProfileVO =
                new SellerProfileVO(conditionVO);

        SellerProfile sellerProfile =
                new SellerProfile(user, sellerProfileVO);

        sellerProfileRepository.save(sellerProfile);
    }
}
