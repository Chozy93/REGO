package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByLevelAndIsActiveOrderBySortOrderAsc(int level, boolean isActive);

	
}
