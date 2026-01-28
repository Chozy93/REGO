package com.itwillbs.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

import com.itwillbs.domain.ProductVO;
import com.itwillbs.dto.MyPageDTO;
import com.itwillbs.dto.ReviewDTO;
import com.itwillbs.entity.Product;
import com.itwillbs.entity.User;
import com.itwillbs.mapper.MypageMapper;
import com.itwillbs.security.CustomUserDetails;
import com.itwillbs.service.ProductService;
import com.itwillbs.service.ReviewService;
import com.itwillbs.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserService userService;
    private final MypageMapper mypageMapper;
    private final ProductService productService;
    private final ReviewService reviewService;
    
    @GetMapping("/sales")
    public String mySalesPage(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";

        // 1. 유저 정보 가져오기 (이메일로 MyPageDTO 조회)
        String email = authentication.getName(); 
        MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
        

        List<Product> entityList = mypageMapper.getSalesListByUserId(mypageInfo.getUserId());

        // 3. 엔티티를 VO로 변환 
        List<ProductVO> salesList = entityList.stream()
            .map(ProductVO::new)
            .collect(Collectors.toList());

        // 4. 화면에 전달
        model.addAttribute("user", mypageInfo);
        model.addAttribute("salesList", salesList);
        
        return "user/my_sales";
    }
    
    
    @GetMapping("/buys")
    public String getPurchaseList(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName(); 
        MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
        Long userId = mypageInfo.getUserId();
        
     // 2. 구매 리스트 가져오기
        List<Product> entityList = mypageMapper.getPurchaseListByUserId(userId);
        
        // 3. VO로 변환하면서 리뷰 여부 체크
        List<ProductVO> purchaseList = entityList.stream()
                .map(entity -> {
                    ProductVO vo = new ProductVO(entity);
                    
                    boolean isReviewed = reviewService.checkIfReviewed(userId, entity.getProductId());
                    vo.setReviewed(isReviewed); 
                    
                    return vo;
                })
                .collect(Collectors.toList());

        model.addAttribute("user", mypageInfo);
        model.addAttribute("purchaseList", purchaseList);
        
        return "user/my_buys"; 
    }
    
    @GetMapping("/likes")
    public String getLikeList(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName(); 
        MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
        
        // 찜한 목록 가져오기
        List<Product> entityList = mypageMapper.getLikeListByUserId(mypageInfo.getUserId());
        
        List<ProductVO> likeList = entityList.stream()
                .map(ProductVO::new)
                .collect(Collectors.toList());

        model.addAttribute("user", mypageInfo);
        model.addAttribute("likeList", likeList);
        
        return "user/my_likes"; 
    }
    
    
    @GetMapping("/reviews")
    public String myReviews(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";


        String email = authentication.getName(); 
        MyPageDTO mypageInfo = mypageMapper.getMyPageInfo(email);
        

        List<ReviewDTO> received = mypageMapper.getReceivedReviews(mypageInfo.getUserId());
        List<ReviewDTO> sent = mypageMapper.getSentReviews(mypageInfo.getUserId());
        
        // 3. 화면에 필요한 데이터 전달
        model.addAttribute("user", mypageInfo);
        model.addAttribute("receivedReviews", received);
        model.addAttribute("sentReviews", sent);
        
        return "user/reviews"; 
    }
    
}
