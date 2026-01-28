package com.itwillbs.repository;

import com.itwillbs.entity.Report;
import com.itwillbs.entity.User;
import com.itwillbs.entity.enumtype.ReportTargetType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByTargetTypeAndTargetIdAndReporterUserId(
        ReportTargetType targetType,
        Long targetId,
        Long userId
    );

     // 최신 신고 3건 (상태 무관)
    List<Report> findTop3ByOrderByCreatedAtDesc();
}

