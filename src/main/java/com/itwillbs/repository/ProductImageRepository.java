package com.itwillbs.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itwillbs.entity.Product;
import com.itwillbs.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
	
	  /* ==================================================
    1. 특정 상품의 이미지 전체 조회 (정렬 기준)
    - 수정 후 재정렬
    - 대표 이미지 결정
 ================================================== */
 List<ProductImage> findByProductOrderBySortOrderAsc(Product product);

 /* ==================================================
    2. 특정 상품의 현재 최대 sortOrder 조회
    - 신규 이미지 추가 시 맨 뒤에 붙이기 위함
    - 이미지 없을 경우 0 반환
 ================================================== */
 @Query(
     "select coalesce(max(pi.sortOrder), 0) " +
     "from ProductImage pi " +
     "where pi.product = :product"
 )
 int findMaxSortOrderByProduct(@Param("product") Product product);

 /* ==================================================
    3. 삭제 대상 이미지 조회 (보안 핵심)
    - image_id + product 조건 둘 다 사용
    - 다른 상품 이미지 삭제 사고 방지
 ================================================== */
 List<ProductImage> findByImageIdInAndProduct(
     List<Long> imageIds,
     Product product
 );

 /* ==================================================
    4. 삭제 대상 이미지 삭제 (bulk delete)
    - 클라우드 이미지 삭제 이후 DB 정리용
    - 반드시 product 조건 포함
 ================================================== */
 void deleteByImageIdInAndProduct(
     List<Long> imageIds,
     Product product
 );

 /* ==================================================
    5. 특정 상품의 이미지 전체 삭제
    - 관리자 기능
    - 상품 삭제 시 연계 처리용
 ================================================== */
 void deleteByProduct(Product product);

 /* ==================================================
    6. 특정 상품의 이미지 개수 조회
    - 이미지 최소 1장 정책
    - UI 제어용
 ================================================== */
 long countByProduct(Product product);

 /* ==================================================
    7. 단일 이미지 조회 (상품 조건 포함)
    - 순서 변경
    - 단건 조작 시 사용
 ================================================== */
 Optional<ProductImage> findByImageIdAndProduct(
     Long imageId,
     Product product
 );
	

}
