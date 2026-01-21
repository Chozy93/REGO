package com.itwillbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.user.NotificationUpdateVO;
import com.itwillbs.entity.NotificationSettings;
import com.itwillbs.entity.User;
import com.itwillbs.repository.NotificationRepository;
import com.itwillbs.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    @Autowired
    private UserRepository userRepository;
    
    public void updateSettings(Long userId, NotificationUpdateVO vo) {
        // 1. 기존 설정이 있는지 확인, 없으면 새로 생성 
        NotificationSettings settings = repository.findById(userId)
        		.orElseGet(() -> {
        		    NotificationSettings newSettings = new NotificationSettings();
        		    User user = userRepository.findById(userId).orElseThrow(); 
        		    newSettings.setUser(user); 
        		    return repository.save(newSettings);
        		});

        // 2. 어떤 타입을 바꿀지 결정
        switch (vo.getType()) {
            case "chat" -> settings.setChatNoti(vo.isStatus());
            case "activity" -> settings.setActivityNoti(vo.isStatus());
            case "marketing" -> settings.setMarketingNoti(vo.isStatus());
            case "total" -> {
                settings.setChatNoti(vo.isStatus());
                settings.setActivityNoti(vo.isStatus());
                settings.setMarketingNoti(vo.isStatus());
            }
        }
    }
    
    public NotificationSettings getSettings(Long userId) {
        return repository.findById(userId)
                .orElseGet(() -> {

                    NotificationSettings newSettings = new NotificationSettings();
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없어요!"));
                    newSettings.setUser(user);
                    // 기본값 세팅
                    newSettings.setChatNoti(true);
                    newSettings.setActivityNoti(true);
                    newSettings.setMarketingNoti(false);
                    return repository.save(newSettings);
                });
    }
}