package com.itwillbs.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {



    long countByCreatedAtBefore(LocalDateTime time);

    List<Product> findTop3ByOrderByCreatedAtDesc();
}
