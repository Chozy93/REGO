package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.Inquiry;
import com.itwillbs.entity.enumtype.InquiryStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InquiryVO {

    private final Long inquiryId;
    private final Long userId;

    private final String title;
    private final String content;

    private final InquiryStatus status;

    private final String answerContent;
    private final Long answeredBy;
    private final LocalDateTime answeredAt;

    private final boolean isPrivate;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /* =========================
       Entity → VO (조회)
    ========================= */
    public InquiryVO(Inquiry entity) {
        this.inquiryId = entity.getInquiryId();
        this.userId = entity.getUser().getUserId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.status = entity.getStatus();
        this.answerContent = entity.getAnswerContent();
        this.answeredBy = entity.getAnsweredBy();
        this.answeredAt = entity.getAnsweredAt();
        this.isPrivate = entity.isPrivate();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    /* =========================
       등록용 생성자
    ========================= */
    public InquiryVO(String title, String content, boolean isPrivate) {
        this.inquiryId = null;
        this.userId = null;
        this.title = title;
        this.content = content;
        this.status = null;
        this.answerContent = null;
        this.answeredBy = null;
        this.answeredAt = null;
        this.isPrivate = isPrivate;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
