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

    @ModelAttribute("headerCategoryListVO")
    public HeaderCategoryListVO headerCategories() {
    	System.out.println("가져온 카테고리 : "+categoryService.getHeaderCategories());
        return categoryService.getHeaderCategories();
    }
}
