package com.itwillbs.service;

import com.itwillbs.dto.SocialAccountDTO;
import com.itwillbs.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 추가 권장

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserMapper userMapper; 

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        String providerId = "";
        if (provider.equals("kakao")) {
            providerId = attributes.get("id").toString();
        } else if (provider.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            providerId = response.get("id").toString();
        } else if (provider.equals("google")) {
            providerId = attributes.get("sub").toString();
        }

        SocialAccountDTO existingAccount = userMapper.findSocialAccount(provider.toUpperCase(), providerId);

        if (existingAccount == null) {
            Map<String, Object> userParams = new HashMap<>();
            userParams.put("username", provider + "_" + providerId);
            userParams.put("nickname", "소셜사용자_" + System.currentTimeMillis() % 1000); 
            
            // 1. 유저 가입 진행
            userMapper.insertUser(userParams); 
            
            // 2. 마이바티스가 채워준 userId 꺼내기 (null 체크 추가)
            Object generatedId = userParams.get("userId");
            if (generatedId == null) {

                throw new RuntimeException("회원가입 실패: 유저 ID를 생성하지 못했습니다.");
            }
            
            long newUserId = Long.parseLong(generatedId.toString());

            // 3. 소셜 계정 정보 연동
            SocialAccountDTO newAccount = new SocialAccountDTO();
            newAccount.setSocialId(System.currentTimeMillis()); 
            newAccount.setProvider(provider.toUpperCase()); 
            newAccount.setProviderId(providerId);
            newAccount.setUserId(String.valueOf(newUserId));
            
            userMapper.insertSocialAccount(newAccount);
            System.out.println("회원가입 및 소셜 연결 완료! 생성된 ID: " + newUserId);
        
        } else {
            System.out.println("기존 소셜 계정 로그인: " + providerId);
        }
        
        return oAuth2User;
    }
}