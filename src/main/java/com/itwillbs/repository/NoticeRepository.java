package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>{
	

	/**
     * 1. 상단 고정(isPinned)된 공지사항 목록 조회
     * 최신순으로 정렬하여 리스트 상단에 노출할 때 사용합니다.
     */
    List<Notice> findByIsPinnedTrueOrderByCreatedAtDesc();

    /**
     * 2. 일반 공지사항 페이징 조회 (활성화된 글만)
     * isActive가 true인 글들만 최신순으로 가져옵니다.
     */
    Page<Notice> findByIsActiveTrue(Pageable pageable);

    /**
     * 3. 카테고리별 공지사항 페이징 조회
     */
    Page<Notice> findByCategoryAndIsActiveTrue(String category, Pageable pageable);

    /**
     * 4. 제목 또는 내용으로 검색 (페이징 포함)
     */
    Page<Notice> findByTitleContainingOrContentContainingAndIsActiveTrue(String title, String content, Pageable pageable);

    /**
     * 5. 조회수 증가 (벌크 연동)
     * @Modifying 어노테이션을 통해 데이터 변경 쿼리임을 명시합니다.
     */
    @Modifying
    @Query("UPDATE Notice n SET n.viewCount = n.viewCount + 1 WHERE n.noticeId = :id")
    int updateViewCount(@Param("id") Long id);
    
    
    
 // [추가] 관리자용: 활성/비활성 상관없이 전체를 최신순으로 페이징 조회
    Page<Notice> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // [추가] 관리자용: 상태별 개수 카운트
    long countByIsActive(boolean isActive);
}
