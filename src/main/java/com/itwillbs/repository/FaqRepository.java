package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long>{
	// 활성화된 FAQ 전체 조회 (카테고리 → 최신순)
	List<Faq> findByIsActiveTrueOrderByFaqCategoryAscCreatedAtDesc();
}
