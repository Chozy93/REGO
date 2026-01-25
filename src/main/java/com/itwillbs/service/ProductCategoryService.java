package com.itwillbs.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.dto.CategoryBaseDTO;
import com.itwillbs.dto.CategoryDTO;
import com.itwillbs.dto.ProductCategoryItemDTO;
import com.itwillbs.mapper.CategoryMapper;
import com.itwillbs.mapper.ProductCategoryMapper;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.view.CategoryGroupVO;
import com.itwillbs.view.CategoryPageVO;
import com.itwillbs.view.MainProductCardVO;
import com.itwillbs.view.ProductListVO;
import com.itwillbs.view.SubCategoryVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final CategoryMapper categoryMapper;
    private final ProductCategoryMapper productCategoryMapper;

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CategoryPageVO getCategoryPage(Long categoryId) {

        // 1️⃣ 로그인 사용자 ID (찜 여부 판단용)
        Long loginUserId = SecurityUtil.getCurrentUserId();

        // 2️⃣ 기준 카테고리 결정
        CategoryBaseDTO baseCategory = resolveBaseCategory(categoryId);

        // 3️⃣ 대분류 확정
        CategoryDTO parentCategory = resolveParentCategory(baseCategory);

        // 4️⃣ 소분류 목록 조회
        List<CategoryDTO> subCategoryDTOs =
                categoryMapper.selectSubCategories(parentCategory.getCategoryId());

        // 5️⃣ 상품 목록 조회
        List<ProductCategoryItemDTO> productDTOs =
                productCategoryMapper.selectProductsByCategory(
                        baseCategory.getCategoryId(),
                        loginUserId
                );

        // 6️⃣ CategoryGroupVO 생성
        CategoryGroupVO categoryGroup =
                createCategoryGroup(
                        parentCategory,
                        subCategoryDTOs,
                        baseCategory.getCategoryId()
                );

        // 7️⃣ ProductListVO 생성
        ProductListVO productList =
                createProductList(productDTOs);

        return new CategoryPageVO(categoryGroup, productList);
    }

    /* ==================================================
       기준 카테고리 결정
    ================================================== */
    private CategoryBaseDTO resolveBaseCategory(Long categoryId) {

        if (categoryId == null) {
            // 기본 대분류 선택 (첫 번째 대분류)
            return categoryMapper.selectCategoryBase(null);
        }

        return categoryMapper.selectCategoryBase(categoryId);
    }

    /* ==================================================
       대분류 확정
    ================================================== */
    
    private CategoryDTO resolveParentCategory(CategoryBaseDTO baseCategory) {

        // level == 1 → 대분류
        if (baseCategory.getLevel() == 1) {
            return categoryMapper.selectParentCategory(
                    baseCategory.getCategoryId()
            );
        }

        // level == 2 → 소분류 → parentId로 대분류 조회
        return categoryMapper.selectParentCategory(
                baseCategory.getParentId()
        );
    }

    /* ==================================================
       CategoryGroupVO 생성
    ================================================== */
    private CategoryGroupVO createCategoryGroup(
            CategoryDTO parentCategory,
            List<CategoryDTO> subCategoryDTOs,
            Long selectedCategoryId
    ) {

        List<SubCategoryVO> subCategories =
                subCategoryDTOs.stream()
                        .map(dto -> new SubCategoryVO(
                                String.valueOf(dto.getCategoryId()),
                                dto.getName(),
                                dto.getCategoryId().equals(selectedCategoryId)
                        ))
                        .toList();

        return new CategoryGroupVO(
                String.valueOf(parentCategory.getCategoryId()),
                parentCategory.getName(),
                subCategories,
                selectedCategoryId != null
                        ? String.valueOf(selectedCategoryId)
                        : String.valueOf(parentCategory.getCategoryId())
        );
    }

    /* ==================================================
       ProductListVO 생성
    ================================================== */
    private ProductListVO createProductList(
            List<ProductCategoryItemDTO> productDTOs
    ) {

        List<MainProductCardVO> cards =
                productDTOs.stream()
                        .map(dto -> new MainProductCardVO(
                                String.valueOf(dto.getProductId()),
                                dto.getTitle(),
                                dto.getPrice(),
                                dto.getImageUrl(),
                                dto.getRegionName(),
                                dto.getCreatedAt().format(TIME_FORMATTER),
                                false,                  // reserved (카테고리 페이지 기준)
                                dto.getLikeCount(),
                                dto.isLiked()
                        ))
                        .toList();

        return new ProductListVO(cards);
    }
    
    /* ==================================================
    부모 카테고리 전체 조회
    - list.html 대분류 탭용
 ================================================== */
 public List<CategoryDTO> getParentCategoryList() {

     // level == 1 (대분류) 전체 조회
     return categoryMapper.selectParentCategoryList();
 }

}
