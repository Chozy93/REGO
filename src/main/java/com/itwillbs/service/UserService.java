package com.itwillbs.service;

import com.itwillbs.domain.LoginRequestVO;
import com.itwillbs.domain.UserSignupConditionVO;
import com.itwillbs.entity.User;
import com.itwillbs.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void join(UserSignupConditionVO condition) {
        // 1. 인증번호 검사
        if (!"123456".equals(condition.getAuthCode())) {
            throw new IllegalArgumentException("인증번호가 틀렸어요!");
        }

        // 2. 새로 만든 생성자로 유저 객체 생성!
        User newUser = new User(
            condition.getEmail(), 
            condition.getPassword(), 
            condition.getUsername(), 
            condition.getNickname(), 
            condition.getPhoneNumber()
        );

        // 3. DB 저장
        userRepository.save(newUser);
    }
    
    public User login(LoginRequestVO request) {
        // 1. 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일이에요."));

        // 2. 비밀번호 확인 (지금은 단순 비교, 나중에 암호화 적용하면 바뀔 부분!)
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸어요.");
        }

        return user;
    }
    
    public boolean isEmailTaken(String email) {
        // UserRepository에서 이메일로 유저를 찾았을 때 존재하면 true 반환
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameTaken(String nickname) {
        // 닉네임으로 유저를 찾아서 있으면 true, 없으면 false!
        return userRepository.existsByNickname(nickname);
    }
}