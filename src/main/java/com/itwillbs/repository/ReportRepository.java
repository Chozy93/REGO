package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.entity.Report;
import com.itwillbs.entity.enumtype.ReportStatus;
import com.itwillbs.entity.enumtype.ReportTargetType;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByTargetTypeAndTargetIdAndReporterUserId(
        ReportTargetType targetType,
        Long targetId,
        Long userId
    );

     // 최신 신고 3건 (상태 무관)
    List<Report> findTop3ByOrderByCreatedAtDesc();
    
    
 // 전체 목록 (최신순)
    Page<Report> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // 상태별 필터링
    Page<Report> findByStatusOrderByCreatedAtDesc(ReportStatus status, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = :status")
    long countByStatus(@Param("status") ReportStatus status);

    // 검색 (제목/내용 키워드 - 필요시)
    Page<Report> findByReasonContainingOrDetailContaining(String reason, String detail, Pageable pageable);
}



