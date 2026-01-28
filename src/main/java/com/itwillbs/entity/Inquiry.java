package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.entity.enumtype.InquiryStatus;
import com.itwillbs.entity.enumtype.InquiryType;

@Entity
@Table(name = "inquiries")
@Getter
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================
       작성자
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /* =========================
       문의 유형
    ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_type", nullable = false, length = 30)
    private InquiryType inquiryType;

    /* =========================
       내용
    ========================= */
    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String content;

    /* =========================
       결제 / 주문 연관
    ========================= */
    @Column(name = "order_id")
    private Long orderId;

    /* =========================
       상태
    ========================= */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InquiryStatus status;

    /* =========================
       답변
    ========================= */
    @Column(name = "answer_content", length = 255)
    private String answerContent;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    @Column(name = "answered_by")
    private Long answeredBy;

    /* =========================
       시간
    ========================= */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /* =========================
       생성 팩토리
    ========================= */
    public static Inquiry create(
            User user,
            InquiryType inquiryType,
            String title,
            String content,
            Long orderId
    ) {
        Inquiry inquiry = new Inquiry();
        inquiry.user = user;
        inquiry.inquiryType = inquiryType;
        inquiry.title = title;
        inquiry.content = content;
        inquiry.orderId = orderId;
        inquiry.status = InquiryStatus.PENDING;
        inquiry.createdAt = LocalDateTime.now();
        inquiry.updatedAt = LocalDateTime.now();
        return inquiry;
    }

    /* =========================
       도메인 행위
    ========================= */
    public void answer(Long adminUserId, String answerContent) {
        this.answerContent = answerContent;
        this.answeredBy = adminUserId;
        this.answeredAt = LocalDateTime.now();
        this.status = InquiryStatus.ANSWERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == InquiryStatus.ANSWERED) {
            throw new IllegalStateException("답변 완료된 문의는 취소할 수 없습니다.");
        }
        this.status = InquiryStatus.CANCELED;
        this.updatedAt = LocalDateTime.now();
    }
}
