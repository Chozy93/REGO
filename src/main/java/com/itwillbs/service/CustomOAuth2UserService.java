package com.itwillbs.service;

import com.itwillbs.dto.SocialAccountDTO;
import com.itwillbs.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserMapper userMapper; 
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        String providerId = extractProviderId(provider, attributes);
        String finalProviderId = providerId.length() > 20 ? providerId.substring(0, 20) : providerId;

        // 1. 먼저 이 소셜 계정 자체가 등록되어 있는지 확인
        SocialAccountDTO existingAccount = userMapper.findSocialAccount(provider.toUpperCase(), finalProviderId);

        if (existingAccount == null) {
            // 2. 소셜 계정은 없지만, 이메일이 같은 기존 유저가 있는지 확인
            String email = extractEmail(provider, attributes, finalProviderId);
            Map<String, Object> existingUser = userMapper.findUserByEmail(email);
            
            long targetUserId;

            if (existingUser != null) {
                // [케이스 A] 이미 가입된 이메일이 있음 -> 이 유저 ID를 그대로 사용!
                targetUserId = Long.parseLong(existingUser.get("userId").toString());
                System.out.println("기존 이메일 유저와 연결합니다. ID: " + targetUserId);
            } else {
                // [케이스 B] 진짜 처음 온 사람 -> 새롭게 회원가입 진행
                Map<String, Object> userParams = new HashMap<>();
                userParams.put("username", provider + "_" + finalProviderId);
                userParams.put("nickname", "소셜사용자_" + System.currentTimeMillis() % 1000);
                userParams.put("email", email);
                
                // 1. 보안을 위해 20자리 랜덤 비밀번호 생성
                String rawPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
                // 2. 암호화해서 DB에 넣기
                userParams.put("password", passwordEncoder.encode(rawPassword)); 
                
                // 전화번호는 아까처럼 20자 맞춰서 임시 생성
                String tempPhone = "TMP_" + finalProviderId;
                userParams.put("phoneNumber", tempPhone.length() > 20 ? tempPhone.substring(0, 20) : tempPhone); 
                
                userMapper.insertUser(userParams);
                
                Object generatedId = userParams.get("userId");
                if (generatedId == null) throw new RuntimeException("회원가입 실패");
                targetUserId = Long.parseLong(generatedId.toString());
                System.out.println("신규 회원가입 성공! ID: " + targetUserId);
            }

            // 3. 소셜 계정 정보를 SocialAccount 테이블에 저장 (연결)
            SocialAccountDTO newAccount = new SocialAccountDTO();
            newAccount.setSocialId(System.currentTimeMillis()); 
            newAccount.setProvider(provider.toUpperCase()); 
            newAccount.setProviderId(finalProviderId); 
            newAccount.setUserId(String.valueOf(targetUserId));

            userMapper.insertSocialAccount(newAccount);
            System.out.println("소셜 계정 연결 완료!");     
        } else {
            System.out.println("기존 소셜 계정 로그인 성공: " + finalProviderId);
        }
        
        return oAuth2User;
    }

    // 소셜 아이디 추출 헬퍼 메서드
    private String extractProviderId(String provider, Map<String, Object> attributes) {
        if (provider.equals("kakao")) return attributes.get("id").toString();
        if (provider.equals("naver")) return ((Map<String, Object>) attributes.get("response")).get("id").toString();
        return attributes.get("sub").toString();
    }

    // 이메일 추출 헬퍼 메서드
    private String extractEmail(String provider, Map<String, Object> attributes, String finalProviderId) {
        if (provider.equals("kakao")) {
            Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
            return (account != null && account.get("email") != null) ? account.get("email").toString() : finalProviderId + "@kakao.temp";
        }
        if (provider.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return (response != null && response.get("email") != null) ? response.get("email").toString() : finalProviderId + "@naver.temp";
        }
        return attributes.get("email") != null ? attributes.get("email").toString() : finalProviderId + "@google.temp";
    }
}