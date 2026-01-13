package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwillbs.domain.LoginRequestVO;
import com.itwillbs.domain.UserSignupConditionVO;
import com.itwillbs.entity.User;
import com.itwillbs.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	
	// 로그인 페이지
	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}

	@PostMapping("/login")
	public String login(LoginRequestVO request, HttpSession session, RedirectAttributes rttr) {
	    try {
	        User loginUser = userService.login(request);
	        session.setAttribute("loginUser", loginUser);
	        return "redirect:/"; 
	    } catch (IllegalArgumentException e) {
	        String errorCode = "error";
	        if (e.getMessage().contains("이메일")) errorCode = "no_user";
	        if (e.getMessage().contains("비밀번호")) errorCode = "wrong_pw";
	        
	        return "redirect:/login?status=" + errorCode;
	    }
	}
	
	// 아이디 찾기 1단계
    @GetMapping("/login/id_find1")
    public String idFind1() {
        return "auth/id_find1";
    }

    // 비밀번호 찾기 1단계
    @GetMapping("/login/pass_find1")
    public String passFind1() {
        return "auth/pass_find1";
    }

	
	
	
	
	
	
	
	
	    // 1단계 화면 (약관동의)
	    @GetMapping("/signup/step1")
	    public String signupStep1() {
	        return "auth/signup1";
	    }

	    // 2단계 화면 (정보입력 페이지 열기)
	    @GetMapping("/signup/step2")
	    public String signupStep2Page() {
	        return "auth/signup2";
	    }

	    @PostMapping("/signup/step2")
	    public String register(UserSignupConditionVO condition) { 
	        System.out.println("화면에서 넘어온 데이터: " + condition.toString()); 
	        try {
	        	if (condition.getUsername() == null || !condition.getUsername().matches("^[가-힣]{2,5}$")) {
	                System.out.println("유효하지 않은 이름 입력됨: " + condition.getUsername());
	                return "redirect:/signup/step2?error=name"; // 이름 에러를 달고 다시 입력창으로 보내기
	            }
	            userService.join(condition);
	            return "redirect:/signup/step3"; 
	        } catch (Exception e) {
	            e.printStackTrace(); 
	            return "auth/signup2";
	        }
	    }
	    
	    @GetMapping("/signup/check-email")
	    @ResponseBody
	    public boolean checkEmail(@RequestParam("email") String email) {
	        // 유저가 있으면 true, 없으면 false 반환
	        return userService.isEmailTaken(email);
	    }
	    
	    public boolean checkNickname(@RequestParam("nickname") String nickname) {
	        // 닉네임이 있으면 true, 없으면 false 반환
	        return userService.isNicknameTaken(nickname); 
	    }
	    
	    // 3단계 화면 (완료 축하)
	    @GetMapping("/signup/step3")
	    public String signupStep3() {
	        return "auth/signup3";
	    }
	}