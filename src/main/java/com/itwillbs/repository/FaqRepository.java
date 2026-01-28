package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long>{
	// 활성화된 FAQ만 카테고리별로 가져오기
    List<Faq> findByIsActiveTrueOrderByFaqCategoryIdAscCreatedAtDesc();
    
    // 특정 카테고리만 필터링할 때
    List<Faq> findByFaqCategoryIdAndIsActiveTrue(String categoryId);
}
