package com.itwillbs.global;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.itwillbs.domain.user.UserVO;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.service.CategoryService;
import com.itwillbs.service.ChatService;
import com.itwillbs.view.HeaderCategoryListVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalViewControllerAdvice {

    private final CategoryService categoryService;
    private final ChatService chatService;

    @ModelAttribute("headerCategoryListVO")
    public HeaderCategoryListVO headerCategories(HttpSession session) {

        try {
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

            session.setAttribute("headerCategoryListVO", categoryList);
            return categoryList;

        } catch (Exception e) {
            // 🔥 에러 페이지에서도 살아야 함
            return HeaderCategoryListVO.empty();
        }
    }

    @ModelAttribute("loginUser")
    public UserVO loginUser() {
        try {
            return SecurityUtil.getCurrentUserVO();
        } catch (Exception e) {
            return null;
        }
    }

    @ModelAttribute("hasUnreadChat")
    public boolean hasUnreadChat() {

        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return false;
            }
            return chatService.hasUnreadChat(userId);
        } catch (Exception e) {
            return false;
        }
    }
}
