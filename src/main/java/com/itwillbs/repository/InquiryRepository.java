package com.itwillbs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.entity.Inquiry;
import com.itwillbs.entity.enumtype.InquiryType;


public interface InquiryRepository extends JpaRepository<Inquiry,Long> {

	@Query("SELECT i FROM Inquiry i WHERE i.user.email = :email " + // userId 대신 email 필드 사용
	           "AND (:type IS NULL OR i.inquiryType = :type)")
	    Page<Inquiry> findMyInquiries(
	        @Param("email") String email, // 파라미터 이름도 의미에 맞게 변경
	        @Param("type") InquiryType type, 
	        Pageable pageable
	    );

   
	
}
