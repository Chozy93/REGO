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

    @Column(name = "content", nullable = false)
    private String content;

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
        this.writerId = writerId;
        this.isActive = vo.isActive();
        this.isPinned = vo.isPinned();
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
        this.isActive = vo.isActive();
        this.isPinned = vo.isPinned();
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       상태 변경
    ========================= */
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
