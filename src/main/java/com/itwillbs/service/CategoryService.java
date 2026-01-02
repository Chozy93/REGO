package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.CategoryVO;
import com.itwillbs.entity.Category;
import com.itwillbs.repository.CategoryRepository;
import com.itwillbs.view.HeaderCategoryListVO;
import com.itwillbs.view.HeaderCategoryNodeVO;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /* =========================
    헤더 카테고리 조회 (UI 전용)
 ========================= */
    @Transactional(readOnly = true)
    public HeaderCategoryListVO getHeaderCategories() {

        // 1. 1레벨(대분류)
        List<Category> parents =
                categoryRepository.findByLevelAndIsActiveOrderBySortOrderAsc(1, true);

        // 2. 부모별로 2레벨 조립
        List<HeaderCategoryNodeVO> nodes = parents.stream()
                .map(parent -> {

                    CategoryVO parentVO = parent.toVO();

                    List<CategoryVO> children = categoryRepository
                            .findByParentCategoryIdAndIsActiveOrderBySortOrderAsc(
                                    parent.getCategoryId(), true
                            )
                            .stream()
                            .map(Category::toVO)
                            .toList();

                    return new HeaderCategoryNodeVO(parentVO, children);
                })
                .toList();

        return new HeaderCategoryListVO(nodes);
    }

}
