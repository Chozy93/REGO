package com.itwillbs.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
		return "auth/completeInfo";
	}

	@PostMapping("/auth/update-phone")
	@ResponseBody
	public ResponseEntity<String> updatePhone(@RequestBody Map<String, String> data, Authentication authentication) {
		String newPhone = data.get("phoneNumber");
		System.out.println("🚩 API로 들어온 새로운 번호: " + newPhone);

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
				userService.updatePhoneNumber(username, newPhone);
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
		return "auth/signup3";
	}
}