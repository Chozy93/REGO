package com.itwillbs.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwillbs.domain.user.UserSignupConditionVO;
import com.itwillbs.entity.User;
import com.itwillbs.mapper.UserMapper;
import com.itwillbs.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private final UserService userService;
	private final UserMapper userMapper;
	
	private Map<String, Object> getVerifiedUserInfo(String impUid) {
		try {
	        org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
	        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
	        headers.set("Authorization", "PortOne im4tZ60IROAfT8VcCioqXCBCElABYFoYidxxVBcYPsRbjZPYCThD79J20OOEn7Iy05W0zzisYfPi2ewz");
	        
	        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
	        String url = "https://api.portone.io/identity-verifications/" + impUid;
	        
	        org.springframework.http.ResponseEntity<Map> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class);
	        Map<String, Object> body = response.getBody();

	        if (body != null && body.containsKey("verifiedCustomer")) {
	            return (Map<String, Object>) body.get("verifiedCustomer"); // 이름, 번호, 생일, 성별이 다 들어있음!
	        }
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	
	
	private String getPhoneNumber(String impUid) {
	    Map<String, Object> userInfo = getVerifiedUserInfo(impUid);
	    if (userInfo != null && userInfo.containsKey("phoneNumber")) {
	        return (String) userInfo.get("phoneNumber");
	    }
	    return null;
	}
	
	
	// 로그인 페이지
	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}

	// 아이디 찾기 1단계
	@GetMapping("/login/id_find1")
	public String idFind1() {
		return "auth/id_find1";
	}

	// 아이디 찾기 처리 (본인인증 후 호출될 주소)
	@PostMapping("/login/find_id_process")
    @ResponseBody
    public ResponseEntity<?> findIdProcess(@RequestBody Map<String, String> payload) {
        String imp_uid = payload.get("imp_uid");
        
        String phoneNumber = getPhoneNumber(imp_uid); 
        
        if (phoneNumber == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "인증 실패"));
        }
        
        String foundEmail = userMapper.findEmailByPhoneNumber(phoneNumber);
        System.out.println("🚩🚩🚩 포트원에서 받아온 번호: " + phoneNumber);
        if (foundEmail != null) {
            return ResponseEntity.ok(Map.of("success", true, "email", foundEmail));
        } else {
            return ResponseEntity.ok(Map.of("success", false, "message", "가입된 이메일이 없어요."));
        }
    }

	// 비밀번호 찾기 1단계
	@GetMapping("/login/pass_find1")
	public String passFind1() {
		return "auth/pass_find1";
	}

	@PostMapping("/login/verify_user_for_pw")
	@ResponseBody
	public Map<String, Object> verifyUserForPw(@RequestBody Map<String, String> request) {
	    String impUid = request.get("imp_uid");
	    String email = request.get("email");
	    
	    Map<String, Object> response = new HashMap<>();
	    
	    // 1. 포트원에서 전화번호 가져오기 
	    String phoneNumber = getPhoneNumber(impUid);
	    
	    if (phoneNumber != null) {
	        // 2. DB에서 이메일과 전화번호가 일치하는 유저가 있는지 확인
	        boolean isMatch = userService.checkUserEmailAndPhone(email, phoneNumber);
	        
	        if (isMatch) {
	            response.put("success", true);
	        } else {
	            response.put("success", false);
	            response.put("message", "입력하신 이메일과 본인인증 정보가 일치하지 않습니다.");
	        }
	    } else {
	        response.put("success", false);
	        response.put("message", "본인인증에 실패했습니다.");
	    }
	    
	    return response;
	}	
	
	@PostMapping("/login/update_password")
	@ResponseBody
	public Map<String, Object> updatePassword(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    String newPassword = request.get("newPassword");
	    
	    Map<String, Object> response = new HashMap<>();
	    
	    try {

	        String encodedPassword = passwordEncoder.encode(newPassword);
	        
	        userService.updateUserPassword(email, encodedPassword);
	        
	        response.put("success", true);
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "비밀번호 변경 중 오류가 발생했습니다.");
	    }
	    
	    return response;
	}
	
	
	// 1단계 화면 (약관동의)
	@GetMapping("/signup/step1")
	public String signupStep1() {
		return "auth2/signup-terms";
	}

	// 2단계 화면 (정보입력 페이지 열기)
	@GetMapping("/signup/step2")
	public String signupStep2Page() {
		return "auth2/signup-step2";
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
			return "auth2/signup-step2";
		}
	}

	@PostMapping("/signup/verify-identity")
	@ResponseBody
	public ResponseEntity<?> verifyIdentity(@RequestBody Map<String, String> payload) {
		
	    String identityVerificationId = payload.get("identityVerificationId");

	    Map<String, Object> userInfo = getVerifiedUserInfo(identityVerificationId); 
	    System.out.println("🚩 포트원 userInfo 전체: " + userInfo);
	    
	    if (userInfo == null) {
	        return ResponseEntity.ok(Map.of("success", false, "message", "본인인증 정보를 가져오지 못했습니다."));
	    }

	    String phoneNumber = (String) userInfo.get("phoneNumber");

	    // 2. 휴대폰 번호 중복 체크
	    if (userService.isPhoneNumberTaken(phoneNumber)) {
	        return ResponseEntity.ok(Map.of(
	            "success", false, 
	            "message", "이미 가입된 휴대폰 번호입니다. 다른 번호를 사용하거나 로그인을 해주세요."
	        ));
	    }

	    Map<String, Object> result = new HashMap<>();
	    result.put("success", true);
	    result.put("phone", userInfo.get("phoneNumber"));
	    result.put("name", userInfo.get("name"));       // 포트원에서 준 실명
	    result.put("birthDate", userInfo.get("birthDate")); // 포트원에서 준 생년월일 (보통 YYYY-MM-DD)
	    result.put("gender", userInfo.get("gender"));    // MALE 혹은 FEMALE
	    
	    return ResponseEntity.ok(result);
	}
	
	
	@GetMapping("/complete-info")
	public String completeInfoPage(Authentication authentication, Model model) {
		if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
			OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

			Map<String, Object> attributes = oAuth2User.getAttributes();
			Map<String, Object> response = (Map<String, Object>) attributes.get("response");

			if (response != null) {
				String email = (String) response.get("email");
				System.out.println("🚩 찾은 이메일: " + email);
				model.addAttribute("email", email);
			}

			if (response != null) {
				String phone = (String) response.get("mobile");
				model.addAttribute("userPhone", phone);
			}
		}
		return "auth2/complete-Info";
	}

	@PostMapping("/auth/update-phone")
	@ResponseBody
	public ResponseEntity<String> updatePhone(@RequestBody Map<String, String> data, Authentication authentication) {
	    String newPhone = data.get("phoneNumber");
	    String newPassword = data.get("password");
	    
	    if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
	        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

			Map<String, Object> attributes = oAuth2User.getAttributes();
			String username = "";

			if (attributes.containsKey("response")) {
				Map<String, Object> response = (Map<String, Object>) attributes.get("response");
				username = (String) response.get("email");
			} else if (attributes.containsKey("email")) {
				username = (String) attributes.get("email");
			} else {
				username = oAuth2User.getName();
			}

			System.out.println("🚩 진짜로 DB와 대조할 식별값: " + username);

			try {
	            userService.updateSocialUserInfo(username, newPhone, newPassword);
	            return ResponseEntity.ok("success");
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("fail");
	        }
	    }
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
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
		return "auth2/signup-complete";
	}
}