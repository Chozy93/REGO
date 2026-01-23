package com.itwillbs.global;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.itwillbs.domain.user.UserVO;
import com.itwillbs.security.CustomUserDetails;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.service.CategoryService;
import com.itwillbs.service.ChatService;
import com.itwillbs.view.HeaderCategoryListVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalViewControllerAdvice  {

    private final CategoryService categoryService;	
    private final ChatService chatService;	

    @ModelAttribute("headerCategoryListVO")
    public HeaderCategoryListVO headerCategories(HttpSession session) {

        HeaderCategoryListVO cached =
                (HeaderCategoryListVO) session.getAttribute("headerCategoryListVO");

            if (cached != null) {
                return cached;
            }

            HeaderCategoryListVO categoryList =
                    categoryService.getHeaderCategories();

            // 🚨 절대 null 금지
            if (categoryList == null) {
                return HeaderCategoryListVO.empty();
            }
        	System.out.println("카테고리리스트"+categoryList);
            session.setAttribute("headerCategoryListVO", categoryList);
            return categoryList;

        } catch (Exception e) {
            // 🔥 에러 페이지에서도 살아야 함
            return HeaderCategoryListVO.empty();
        }

        HeaderCategoryListVO categoryList =
                categoryService.getHeaderCategories();

        session.setAttribute("headerCategoryListVO", categoryList);
        return categoryList;
    }
    
    
    @ModelAttribute("loginUser")
    public UserVO loginUser() {
    	  System.out.println("🔐 loginUser = " + SecurityUtil.getCurrentUserVO());
        return SecurityUtil.getCurrentUserVO();
    }
    
    @ModelAttribute("hasUnreadChat")
    public boolean hasUnreadChat() {

        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return false;
        }

        boolean hasUnread = chatService.hasUnreadChat(userId);
        return hasUnread;
    }
}
