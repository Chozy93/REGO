package com.itwillbs.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.dto.MyPageDTO;
import com.itwillbs.mapper.MypageMapper;

import lombok.RequiredArgsConstructor;

// 마이페이지 컨트롤러
@Controller
@RequiredArgsConstructor
public class UserController {
	final public MypageMapper mypageMapper;
	
	@GetMapping("/mypage")
	public String myPageMain(Authentication authentication, org.springframework.ui.Model model) {
	    // 1. 로그인 확인 (비로그인 처리)
	    if (authentication == null) {
	        return "redirect:/login";
	    }

	    String email = "";
	    if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
	        email = ((org.springframework.security.core.userdetails.UserDetails)authentication.getPrincipal()).getUsername();
	    } else if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
	        Map<String, Object> attributes = ((org.springframework.security.oauth2.core.user.OAuth2User)authentication.getPrincipal()).getAttributes();
	        // 소셜 로그인 제공자마다 이메일 위치가 다를 수 있으니 주의!
	        email = (String) attributes.get("email"); 
	    }

	  
		// 2. 데이터 가져오기
	    MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
	    
	    // 3. 모델에 담기
	    model.addAttribute("user", mypageInfo);

	    return "user/mypage";
	}
	
	// 프로필 수정 페이지 열기
	@GetMapping("/mypage/edit")
    public String editProfile(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";

        String email = getUserEmail(authentication);
        MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
        model.addAttribute("user", mypageInfo);
        
        return "user/profile-edit"; 
    }

    private String getUserEmail(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            Map<String, Object> attributes = ((OAuth2User) authentication.getPrincipal()).getAttributes();
            return (String) attributes.get("email");
        }
        return "";
    }
}

	
	

