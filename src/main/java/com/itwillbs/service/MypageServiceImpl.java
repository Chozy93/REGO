package com.itwillbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.user.UserVO;
import com.itwillbs.mapper.MypageMapper;
import com.itwillbs.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MypageServiceImpl implements MypageService {

    private final MypageMapper mypageMapper; 
    private final UserRepository userRepository;
    
    
    
    @Override
    public void updateUserInfo(Long userId, UserVO updateData) {
        // 1. 기본 정보 업데이트
        mypageMapper.updateUser(userId, updateData);
        
        // 2. 주소 정보 업데이트
        if (updateData.getAddress() != null && !updateData.getAddress().isEmpty()) {
            mypageMapper.updateAddress(userId, updateData.getAddress());
        }
    }
    
    
    @Override
    public void withdrawUser(Long userId) {
        // 1. 리포지토리로 유저 엔티티를 가져옴
        com.itwillbs.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 2. 상태값 변경 (WITHDRAWN으로 바뀜)
        user.withdraw();
        
        userRepository.save(user); 
    }
}
   