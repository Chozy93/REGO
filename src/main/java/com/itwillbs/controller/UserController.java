package com.itwillbs.controller;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.domain.user.NotificationUpdateVO;
import com.itwillbs.domain.user.UserVO;
import com.itwillbs.dto.MyPageDTO;
import com.itwillbs.dto.SocialAccountDTO;
import com.itwillbs.entity.NotificationSettings;
import com.itwillbs.entity.User;
import com.itwillbs.mapper.MypageMapper;
import com.itwillbs.mapper.UserMapper;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.service.CloudinaryImageService;
import com.itwillbs.service.MypageService;
import com.itwillbs.service.NotificationService;
import com.itwillbs.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

// 마이페이지 컨트롤러
@Controller
@RequiredArgsConstructor
public class UserController {
	private final MypageMapper mypageMapper;
	private final MypageService mypageService;
	private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
	
	@Autowired
	private NotificationService notificationService;
	private final CloudinaryImageService cloudinaryImageService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;
	
	
	@GetMapping("/mypage")
	public String myPageMain(Authentication authentication, Model model) {
	    if (authentication == null) return "redirect:/login";

	    String email = getUserEmail(authentication);
	    
	    if (email == null || email.isEmpty()) return "redirect:/login";

	    // 1. 기존 유저 기본 정보 가져오기
	    MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
	    if (mypageInfo == null) return "redirect:/"; 
	    

	    Map<String, Object> sellerProfile = userMapper.findSellerProfileByEmail(email);
	    
	    model.addAttribute("user", mypageInfo);
	    model.addAttribute("sellerProfile", sellerProfile);
	    
	    return "user/mypage";
	}
	
	// 프로필 수정 페이지 열기
	@GetMapping("/mypage/edit")
    public String editProfile(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";

        String email = getUserEmail(authentication);
        MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
        SocialAccountDTO socialInfo = userMapper.findSocialAccountByUserId(mypageInfo.getUserId());
        
        model.addAttribute("user", mypageInfo);
        model.addAttribute("social", socialInfo);
        
        return "user/profile-edit"; 
    }

	private String getUserEmail(Authentication authentication) {
	    Object principal = authentication.getPrincipal();
	    
	    if (principal instanceof UserDetails) {
	        return ((UserDetails) principal).getUsername();
	    } else if (principal instanceof OAuth2User) {
	        Map<String, Object> attributes = ((OAuth2User) principal).getAttributes();
	        

	        if (attributes.containsKey("email")) {
	            return (String) attributes.get("email");
	        }
	        

	        if (attributes.containsKey("response")) {
	            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
	            return (String) response.get("email");
	        }
	        

	        if (attributes.containsKey("kakao_account")) {
	            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
	            return (String) kakaoAccount.get("email");
	        }
	    }
	    return null;
	}
    
    
    @PostMapping("/mypage/update")
    public String updateProfile(@ModelAttribute UserVO updateData, 
                               Authentication authentication,
                               HttpSession session) {
        
        System.out.println("1. 수정 요청 데이터 확인: " + updateData.toString());

        if (authentication == null) {
            return "redirect:/login";
        }

        String email = getUserEmail(authentication);
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");
        

        if (loginUser == null) {
            System.out.println("⚠️ 세션 복구 시작 (이메일: " + email + ")");
            

            MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
            

            loginUser = new UserVO();
            loginUser.setUserId(mypageInfo.getUserId());
            loginUser.setEmail(mypageInfo.getEmail());
            loginUser.setNickname(mypageInfo.getNickname());

            
            // 3. 복구된 정보를 세션에 다시 저장
            session.setAttribute("loginUser", loginUser);
        }

        System.out.println("2. 유저 아이디 확인: " + loginUser.getUserId());
        
        mypageService.updateUserInfo(loginUser.getUserId(), updateData);
        
        // 세션 정보 업데이트
        loginUser.setNickname(updateData.getNickname());
        loginUser.setIntroduction(updateData.getIntroduction());
        loginUser.setAddress(updateData.getAddress());
        loginUser.setPhoneNumber(updateData.getPhoneNumber());
        
        session.setAttribute("loginUser", loginUser);
        
        return "redirect:/mypage";
    }
    
    @PostMapping("/mypage/check-password")
    @ResponseBody
    public ResponseEntity<?> checkPassword(@RequestParam("currentPassword") String currentPassword, 
                                           Authentication authentication) {
        System.out.println("🔍 1. 프론트 비번 확인: [" + currentPassword + "]");

        try {
            if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");

            String email = getUserEmail(authentication);
            MyPageDTO user = mypageMapper.getMyPageInfo(email);
            
            if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 없음");

            System.out.println("🔍 2. DB 조회 성공 (ID: " + user.getUserId() + ")");

            String encodedPassword = mypageMapper.getUserPassword(user.getUserId());
            System.out.println("🔍 3. DB 비번 확인: [" + encodedPassword + "]");


            if (encodedPassword == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("소셜 계정은 비밀번호가 없습니다.");
            }

            boolean isMatch = passwordEncoder.matches(currentPassword, encodedPassword);
            System.out.println("🔍 4. 일치 여부: " + isMatch);

            if (isMatch) return ResponseEntity.ok().build();
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("불일치");

        } catch (Exception e) {
            System.out.println("❌ 에러 발생!!! 원인: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/mypage/update-password")
    @ResponseBody
    public ResponseEntity<?> updatePassword(@RequestParam("newPassword") String newPassword, 
                                           Authentication authentication) {
        try {
            if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            String email = getUserEmail(authentication);
            MyPageDTO user = mypageMapper.getMyPageInfo(email);
            
            System.out.println("🔍 비번 변경 시작 (ID: " + user.getUserId() + ")");

            // 새 비밀번호 암호화
            String encodedPwd = passwordEncoder.encode(newPassword);
            
            // DB 업데이트
            mypageMapper.updatePassword(user.getUserId(), encodedPwd);
            System.out.println("✅ 비번 변경 완료!");

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/mypage/upload-profile")
    @ResponseBody
    public ResponseEntity<?> uploadProfile(
            @RequestParam("profileFile") MultipartFile file,
            Authentication authentication,
            HttpSession session
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 비어있습니다.");
            }

            /* =========================
               1. 유저 식별
            ========================= */
            String email = getUserEmail(authentication);
            UserVO loginUser = (UserVO) session.getAttribute("loginUser");

            if (loginUser == null) {
                MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
                loginUser = new UserVO();
                loginUser.setUserId(mypageInfo.getUserId());
                loginUser.setEmail(mypageInfo.getEmail());
                session.setAttribute("loginUser", loginUser);
            }

            /* =========================
               2. Cloudinary 업로드
            ========================= */
            String imageUrl =
                cloudinaryImageService.uploadProfileImage(
                    loginUser.getUserId(),
                    file
                );

            /* =========================
               3. DB 업데이트
            ========================= */
            mypageMapper.updateProfileImg(loginUser.getUserId(), imageUrl);

            /* =========================
               4. 세션 반영
            ========================= */
            loginUser.setProfileImg(imageUrl);
            session.setAttribute("loginUser", loginUser);

            System.out.println("✅ 프로필 이미지 변경 성공: " + imageUrl);

            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    
    
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
                return (Map<String, Object>) body.get("verifiedCustomer");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @PostMapping("/mypage/update-phone-verified")
    @ResponseBody
    public ResponseEntity<?> updatePhoneVerified(@RequestBody Map<String, String> payload, 
                                                Authentication authentication) {
        String impUid = payload.get("imp_uid");
        
        Map<String, Object> userInfo = getVerifiedUserInfo(impUid); 
        
        if (userInfo == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "인증 정보를 가져오지 못했습니다."));
        }

        String verifiedPhone = (String) userInfo.get("phoneNumber");
        String email = getUserEmail(authentication);
        
        try {
            // MypageService 호출
            mypageService.updatePhoneNumberByEmail(email, verifiedPhone);
            return ResponseEntity.ok(Map.of("success", true, "phoneNumber", verifiedPhone));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "업데이트 중 오류 발생"));
        }
    }
    
 // 설정 메인 페이지
    @GetMapping("/mypage/settings")
    public String settings(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";
        
        String email = getUserEmail(authentication);
        MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
        model.addAttribute("user", mypageInfo);
        
        return "user/settings"; 
    }

    // 알림 설정 페이지
    @GetMapping("/mypage/settings/notifications")
    public String notificationSettings(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/login";
        

        String email = getUserEmail(authentication); 
        Long userId = userRepository.findByEmail(email)
                                    .map(user -> user.getUserId())
                                    .orElseThrow(() -> new RuntimeException("유저 없음"));

        NotificationSettings settings = notificationService.getSettings(userId);
        model.addAttribute("settings", settings);
        
        return "user/notifications"; 
    }
    
    
    @PostMapping("/mypage/settings/notifications/update")
    @ResponseBody
    public ResponseEntity<String> updateNotification(@RequestBody NotificationUpdateVO vo, Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).build();
        

        String email = authentication.getName(); 
        

        Long userId = userRepository.findByEmail(email)
                                    .map(user -> user.getUserId())
                                    .orElseThrow(() -> new RuntimeException("유저 없음"));

        notificationService.updateSettings(userId, vo);
        return ResponseEntity.ok("Success");
    }

 //   1. 회원 탈퇴 페이지 이동 (GET)
    @GetMapping("/mypage/settings/withdraw")
    public String withdrawPage(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";
        

        String email = getUserEmail(authentication);
        MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
        model.addAttribute("user", mypageInfo);
        
        return "user/withdraw";
    }

 // 2. 실제 탈퇴 처리 
    @PostMapping("/mypage/settings/withdraw/process")
    @ResponseBody
    public ResponseEntity<?> processWithdraw(@RequestParam("password") String password, 
                                            Authentication authentication,
                                            HttpSession session) {
        try {
            String email = getUserEmail(authentication);
            MyPageDTO user = mypageMapper.getMyPageInfo(email);
            
            // 1. 비밀번호 검증
            String encodedPassword = mypageMapper.getUserPassword(user.getUserId());
            if (!passwordEncoder.matches(password, encodedPassword)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
            }

            // 2. 탈퇴 서비스 호출
            mypageService.withdrawUser(user.getUserId());

            // 3. 세션 무효화
            session.invalidate();

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("탈퇴 처리 중 오류가 발생했습니다.");
        }
    }

	
}
	

