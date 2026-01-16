package com.itwillbs.domain;

import java.time.format.DateTimeFormatter;

import com.itwillbs.entity.Inquiry;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InquiryVO {

    private final Long inquiryId;
    private final Long userId;

    private final String title;
    private final String content;

    /* enum 제거 */
    private final String statusCode;
    private final String statusLabel;

    private final String answerContent;
    private final Long answeredBy;
    private final String answeredAt;

    private final boolean isPrivate;

    private final String createdAt;
    private final String updatedAt;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /* =========================
       Entity → VO (조회)
    ========================= */
    public InquiryVO(Inquiry entity) {
        this.inquiryId = entity.getInquiryId();
        this.userId = entity.getUser().getUserId();
        this.title = entity.getTitle();
        this.content = entity.getContent();

        this.statusCode = entity.getStatus().name();
        this.statusLabel = entity.getStatus().getLabel();

        this.answerContent = entity.getAnswerContent();
        this.answeredBy = entity.getAnsweredBy();
        this.answeredAt = entity.getAnsweredAt() != null
                ? entity.getAnsweredAt().format(FORMATTER)
                : "";

        this.isPrivate = entity.isPrivate();

        this.createdAt = entity.getCreatedAt().format(FORMATTER);
        this.updatedAt = entity.getUpdatedAt() != null
                ? entity.getUpdatedAt().format(FORMATTER)
                : "";
    }

    /* =========================
       등록용 생성자
    ========================= */
    public InquiryVO(String title, String content, boolean isPrivate) {
        this.inquiryId = null;
        this.userId = null;
        this.title = title;
        this.content = content;

        this.statusCode = "";
        this.statusLabel = "";

        this.answerContent = "";
        this.answeredBy = null;
        this.answeredAt = "";

        this.isPrivate = isPrivate;

        this.createdAt = "";
        this.updatedAt = "";
    }
}
