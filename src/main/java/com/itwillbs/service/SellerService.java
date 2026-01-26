package com.itwillbs.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.domain.SellerProfileVO;
import com.itwillbs.dto.ProductRegionDTO;
import com.itwillbs.entity.Category;
import com.itwillbs.entity.Product;
import com.itwillbs.entity.ProductImage;
import com.itwillbs.entity.SellerProfile;
import com.itwillbs.entity.User;
import com.itwillbs.mapper.SellerMapper;
import com.itwillbs.repository.CategoryRepository;
import com.itwillbs.repository.ProductImageRepository;
import com.itwillbs.repository.ProductRepository;
import com.itwillbs.repository.SellerProfileRepository;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.view.condition.SellerProductRegisterConditionVO;
import com.itwillbs.view.condition.SellerRegisterConditionVO;
import com.itwillbs.view.seller.ReviewFilterConditionVO;
import com.itwillbs.view.seller.SellerProfilePageViewVO;
import com.itwillbs.view.seller.SellerProfileViewVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerService {
	
	private final CloudinaryImageService cloudinaryImageService;
    private final SellerProfileRepository sellerProfileRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final SellerMapper sellerMapper;
    /* =========================
	    판매자 프로필 존재 여부
	 ========================= */
	 @Transactional(readOnly = true)
	 public boolean hasSellerProfile(User user) {
	     return sellerProfileRepository.existsById(user.getUserId());
	 }
    
	 //판매자 프로필 생성
	 @Transactional
	 public void createSellerProfile(User user,
	                                 SellerRegisterConditionVO conditionVO) {

	     Long userId = user.getUserId();

	     if (sellerProfileRepository.existsBySeller_UserId(userId)) {
	         throw new IllegalStateException("이미 판매자 프로필이 존재합니다.");
	     }

	     // 🔑 FK 용도 → 프록시가 베스트
	     User managedUser = userRepository.getReferenceById(userId);

	     SellerProfileVO sellerProfileVO =
	             new SellerProfileVO(conditionVO);

	     SellerProfile sellerProfile =
	             new SellerProfile(managedUser, sellerProfileVO);

	     sellerProfileRepository.save(sellerProfile);
	 }
	 
	 
	 /* =========================
     판매자 프로필 페이지 조회
  ========================= */
	 @Transactional(readOnly = true)
	    public SellerProfilePageViewVO getSellerProfilePage(
	            Long sellerId,
	            ReviewFilterConditionVO conditionVO,
	            int offset,
	            int size
	    ) {
	        SellerProfileViewVO profile =
	                sellerMapper.selectSellerProfile(sellerId);

	        if (profile == null) {
	            throw new IllegalArgumentException("판매자 프로필이 존재하지 않습니다.");
	        }

	        return new SellerProfilePageViewVO(
	                profile,
	                sellerMapper.selectSellingProducts(sellerId),
	                sellerMapper.selectCompletedProducts(sellerId),
	                sellerMapper.selectSellerReviews(sellerId, conditionVO, offset, size)
	        );
	    }
	 
	 //상품 지역 검색
	 public List<ProductRegionDTO> searchProductRegions(String keyword) {

	        if (!StringUtils.hasText(keyword) || keyword.trim().length() < 2) {
	            return List.of();
	        }

	        return sellerMapper.searchProductRegions(keyword.trim());
	    }
	 
	
	 @Transactional
	    public Long productRegister(
	            SellerProductRegisterConditionVO conditionVO,
	            List<MultipartFile> images
	    ) {
	        /* =========================
	           1. 판매자
	        ========================= */
	        User seller = SecurityUtil.getCurrentUser();
	        if (seller == null) {
	            throw new IllegalStateException("로그인 유저가 없습니다.");
	        }

	        /* =========================
	           2. 카테고리
	        ========================= */
	        Category category = categoryRepository.findById(conditionVO.getCategoryId())
	            .orElseThrow(() ->
	                new IllegalArgumentException("유효하지 않은 카테고리입니다.")
	            );

	  
	        List<String> imageUrls = List.of();
	        String mainImageUrl = null;

	        /* =========================
	           이미지 업로드 (선택)
	        ========================= */
	        if (images != null && !images.isEmpty()) {
	            imageUrls = cloudinaryImageService.upload(images);
	            mainImageUrl = imageUrls.get(0);
	        }

	        /* =========================
	           4. Product 생성
	        ========================= */
	        Product product = Product.createByRegisterCondition(
	            seller,
	            category,
	            conditionVO,
	            mainImageUrl
	        );

	        productRepository.save(product);

	        /* =========================
	           5. ProductImage 생성
	           - 정렬 순서 서비스 책임
	        ========================= */
	        if (!imageUrls.isEmpty()) {
	            int sortOrder = 1;

	            for (String url : imageUrls) {
	                ProductImage image =
	                    ProductImage.create(product, url, sortOrder++);
	                productImageRepository.save(image);
	            }
	        }
	        /* =========================
	           6. 결과 반환
	        ========================= */
	        return product.getProductId();
	    }
	

}



