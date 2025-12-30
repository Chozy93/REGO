package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.InquiryVO;
import com.itwillbs.entity.enumtype.InquiryStatus;

@Entity
@Table(name = "inquiries")
@Getter
public class Inquiry {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long inquiryId;

    /* =========================
       문의 작성자
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_inquiries_user")
    )
    private User user;

    /* =========================
       제목 / 내용
    ========================= */
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    /* =========================
       문의 상태
    ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InquiryStatus status;

    /* =========================
       답변 정보
    ========================= */
    @Column(name = "answer_content")
    private String answerContent;

    @Column(name = "answered_by")
    private Long answeredBy; // 관리자 user_id

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    /* =========================
       비공개 여부
    ========================= */
    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

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
    protected Inquiry() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public Inquiry(User user, InquiryVO vo) {
        this.user = user;
        this.title = vo.getTitle();
        this.content = vo.getContent();
        this.isPrivate = vo.isPrivate();
        this.status = InquiryStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public InquiryVO toVO() {
        return new InquiryVO(this);
    }

    /* =========================
       상태 변경 로직
    ========================= */
    public void answer(String answerContent, Long adminId) {
        this.answerContent = answerContent;
        this.answeredBy = adminId;
        this.answeredAt = LocalDateTime.now();
        this.status = InquiryStatus.ANSWERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = InquiryStatus.CANCELED;
        this.updatedAt = LocalDateTime.now();
    }
}
