package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Inquiry;


public interface InquiryRepository extends JpaRepository<Inquiry,Long> {

}
