package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    // 최신 신고 3건 (상태 무관)
    List<Report> findTop3ByOrderByCreatedAtDesc();
}
