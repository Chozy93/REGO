package com.itwillbs.service;

import com.itwillbs.domain.user.UserVO;

public interface MypageService {
    void updateUserInfo(Long userId, UserVO updateData);
    void withdrawUser(Long userId);
}