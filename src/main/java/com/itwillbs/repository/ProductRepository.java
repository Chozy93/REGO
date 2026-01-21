package com.itwillbs.repository;

import com.itwillbs.entity.Product;
import com.itwillbs.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByBuyer_UserId(Long userId, Pageable pageable);
    
    
}