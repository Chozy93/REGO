package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.CategoryIcon;

public interface CategoryIconRepository
        extends JpaRepository<CategoryIcon, Long> {
	
}