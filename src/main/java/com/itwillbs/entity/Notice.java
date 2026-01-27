package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.NoticeVO;

@Entity
@Table(name = "notices")
@Getter
public class Notice {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long noticeId;

    /* =========================
       제목 / 내용
    ========================= */
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    /* 1. TEXT 타입을 위한 수정: @Lob 또는 columnDefinition 사용 */
    @Lob 
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    
    
    /* 2. 카테고리 필드 추가 */
    @Column(name = "category", length = 50, nullable = false)
    private String category;
    
    
    /* 3. 조회수 필드 추가 (기본값 0) */
    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    /* =========================
       작성자 (관리자 ID)
    ========================= */
    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    /* =========================
       노출 / 고정 여부
    ========================= */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "is_pinned", nullable = false)
    private boolean isPinned;

    /* =========================
       날짜
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected Notice() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public Notice(Long writerId, NoticeVO vo) {
        this.title = vo.getTitle();
        this.content = vo.getContent();
        this.category = vo.getCategory(); // 추가
        this.writerId = writerId;
        this.isActive = vo.isActive();
        this.isPinned = vo.isPinned();
        this.viewCount = 0;               // 추가
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public NoticeVO toVO() {
        return new NoticeVO(this);
    }

    /* =========================
       수정
    ========================= */
    public void update(NoticeVO vo) {
        this.title = vo.getTitle();
        this.content = vo.getContent();
        this.category = vo.getCategory(); // 추가
        this.isActive = vo.isActive();
        this.isPinned = vo.isPinned();
        this.updatedAt = LocalDateTime.now();
    }
    


    /* =========================
       상태 변경
    ========================= */
    /* 4. 조회수 증가 메서드 추가 */
    public void increaseViewCount() {
        this.viewCount++;
    }
    
    
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void pin() {
        this.isPinned = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void unpin() {
        this.isPinned = false;
        this.updatedAt = LocalDateTime.now();
    }
}
