package com.itwillbs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.dto.ProductListByCategoryDTO;
import com.itwillbs.mapper.CategoryMapper;
import com.itwillbs.mapper.ProductListMapper;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.view.MainProductCardVO;
import com.itwillbs.view.ProductListPageVO;
import com.itwillbs.view.condition.ProductListConditionVO;
import com.itwillbs.view.product.list.CategoryBarVO;
import com.itwillbs.view.product.list.CategoryTabVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductListService {

    private final ProductListMapper productListMapper;
    private final CategoryMapper categoryMapper;

    /* =========================
       상품 리스트 페이지 (카테고리 기준)
       + LIST00_CATEGORY_BAR
    ========================= */
    public ProductListPageVO getProductListPage(
            ProductListConditionVO condition
    ) {

        Long categoryId = condition.getCategoryId();

        // 1️⃣ 로그인 사용자 ID (찜 상태용)
        Long loginUserId = SecurityUtil.getCurrentUserId();

        // 2️⃣ 카테고리 바 생성
        CategoryBarVO categoryBar =
                createCategoryBar(categoryId);

        // 3️⃣ 상품 조회 (대분류 → 자식 포함)
        List<ProductListByCategoryDTO> dtos =
                productListMapper
                        .selectProductsByCategoryWithChildren(
                                categoryId,
                                loginUserId
                        );

        // 4️⃣ 카드 VO 변환
        List<MainProductCardVO> cards = dtos.stream()
                .map(dto -> new MainProductCardVO(
                        String.valueOf(dto.getProductId()), // id
                        dto.getTitle(),                      // title
                        dto.getPrice(),                      // price
                        dto.getThumbnailUrl(),               // img
                        dto.getRegionName(),                 // loc
                        dto.getCreatedTime().toString(),     // time
                        false,                               // reserved
                        dto.getLikeCount(),                  // likeCount
                        dto.isLiked()                        // liked
                ))
                .toList();

        // 5️⃣ PageVO 조립 (setter 사용 ❌)
        return new ProductListPageVO(
                categoryBar,
                cards
        );
    }

    /* =========================
       LIST00_CATEGORY_BAR 생성
    ========================= */
    private CategoryBarVO createCategoryBar(Long categoryId) {

        // 1️⃣ 현재 카테고리 이름
        String categoryName =
                categoryMapper.selectCategoryNameById(categoryId);

        // 2️⃣ 자식 카테고리 조회
        var children =
                categoryMapper.selectChildrenByParentId(categoryId);

        // 자식이 있으면 = 대분류
        boolean hasChildren = !children.isEmpty();

        List<CategoryTabVO> tabs = new ArrayList<>();

        // 3️⃣ 대분류일 경우 "전체보기" 탭
        if (hasChildren) {
            tabs.add(new CategoryTabVO(
                    String.valueOf(categoryId),
                    "전체보기",
                    true   // LIST00 기준: 기본 active
            ));
        }

        // 4️⃣ 자식 카테고리 탭
        for (var child : children) {
            tabs.add(new CategoryTabVO(
                    String.valueOf(child.getCategoryId()),
                    child.getName(),
                    false
            ));
        }

        // 5️⃣ CategoryBarVO 완성
        return new CategoryBarVO(
                categoryName,
                hasChildren,
                tabs
        );
    }
}
