package com.itwillbs.service;

import com.itwillbs.domain.user.UserSignupConditionVO;
import com.itwillbs.entity.User;
import com.itwillbs.mapper.UserMapper;
import com.itwillbs.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    /**
     * 회원 가입
     */
    public void join(UserSignupConditionVO condition) {

        // 2. 이메일 중복 검증
        if (userRepository.findByEmail(condition.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        // 3. 비밀번호 인코딩 (Security 책임 연계)
        String encodedPassword = passwordEncoder.encode(condition.getPassword());
        // 4. Entity 생성 (기본 상태는 Entity 내부에서 결정)
        User newUser = new User(
                condition.getEmail(),
                encodedPassword,
                condition.getUsername(),
                condition.getNickname(),
                condition.getPhoneNumber(),
                condition.getGender()
                
                
        );
        newUser.setProfileImg("https://lh3.googleusercontent.com/aida-public/AB6AXuD0ObRBFDRs_vob6idb6aYKUuIove2YF-wbvYCHg-2sm6qoyedO7RYGPgbD3YUragy7aKMdHZaGuFf9n2VN8bfXNLuaHQv41ulrqhVYIpmk3x64L5NlUFVVfia-ExqeHUsxo5vgQfQPtrASlzehup3VxN0K1KuHLfM_Jo4LSDScARNe9G-rzONQqnH5Zobrl4cD0Z9vAbXnHqFPATdIE6yqtnbrqSfNs-liXa-Ege7QLNN9juuw7GAvbZhH4XK8XgkJEehsTeeKTVY");
        // 5. 저장
        userRepository.save(newUser);
    }

    /**
     * 이메일 중복 검증 전용 메서드
     * - 성공/실패 boolean 반환 ❌
     * - 실패는 예외로만 표현
     */
    @Transactional(readOnly = true)
    public void validateEmailAvailable(String email) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }
    

    
    public boolean isEmailTaken(String email) {
        // UserRepository에서 이메일로 유저를 찾았을 때 존재하면 true 반환
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameTaken(String nickname) {
        // 닉네임으로 유저를 찾아서 있으면 true, 없으면 false!
        return userRepository.existsByNickname(nickname);
    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(email).orElse(null);
    }

	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	public void updateSocialUserInfo(String username, String newPhone, String rawPassword) {

	    String encodedPassword = passwordEncoder.encode(rawPassword);
	    userMapper.updateSocialInfo(username, newPhone, encodedPassword);
	}

	public boolean checkUserEmailAndPhone(String email, String phoneNumber) {
	    return userMapper.countByEmailAndPhone(email, phoneNumber) > 0;
	}

	public void updateUserPassword(String email, String newPassword) {
	    userMapper.updateUserPassword(email, newPassword);
	}
	
	public boolean isPhoneNumberTaken(String phoneNumber) {
	    return userMapper.countByPhoneNumber(phoneNumber) > 0;
	}
	
    }
