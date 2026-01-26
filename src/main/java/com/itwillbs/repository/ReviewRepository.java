package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
