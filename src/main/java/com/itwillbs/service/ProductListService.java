package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.itwillbs.dto.ProductListByCategoryDTO;
import com.itwillbs.mapper.CategoryMapper;
import com.itwillbs.mapper.ProductListMapper;
import com.itwillbs.view.MainProductCardVO;
import com.itwillbs.view.ProductListPageVO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductListService {

    private final ProductListMapper productListMapper;
    private final CategoryMapper categoryMapper;
    private final ProductLikeService productLikeService;

   
    public ProductListPageVO getProductsByCategory(Long categoryId) {

        // 1️⃣ 카테고리 이름
        String categoryName =
                categoryMapper.selectCategoryNameById(categoryId);

        // 2️⃣ 상품 조회
        List<ProductListByCategoryDTO> dtos =
                productListMapper.selectProductsByCategoryWithChildren(categoryId);

        // 3️⃣ 카드 조립
        List<MainProductCardVO> cards = dtos.stream()
                .map(dto -> {

                    // 🔥 로그인 전 더미 사용자
                    boolean liked =
                            productLikeService.isLiked(dto.getProductId(), "testUser");

                    int likeCount =
                            productLikeService.getLikeCount(dto.getProductId());

                    return new MainProductCardVO(
                            String.valueOf(dto.getProductId()),
                            dto.getTitle(),
                            dto.getPrice(),
                            dto.getThumbnailUrl(),
                            dto.getRegionName(),
                            dto.getCreatedTime().toString(),
                            liked,
                            likeCount,
                            false
                    );
                })
                .toList();

        // 4️⃣ Page VO 조립
        ProductListPageVO page = new ProductListPageVO(categoryId, cards);
        page.setCategoryName(categoryName);

        return page;
    }
}
