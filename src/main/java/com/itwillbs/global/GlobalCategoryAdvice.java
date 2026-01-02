package com.itwillbs.global;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.itwillbs.service.CategoryService;
import com.itwillbs.view.HeaderCategoryListVO;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalCategoryAdvice {

    private final CategoryService categoryService;	

    @ModelAttribute("headerCategoryVO")
    public HeaderCategoryListVO headerCategories() {
        return categoryService.getHeaderCategories();
    }
}
