package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.Notice;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NoticeVO {

    private final Long noticeId;
    private final String title;
    private final String content;
    private final Long writerId;
    private final boolean isActive;
    private final boolean isPinned;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /* =========================
       Entity → VO (조회)
    ========================= */
    public NoticeVO(Notice entity) {
        this.noticeId = entity.getNoticeId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.writerId = entity.getWriterId();
        this.isActive = entity.isActive();
        this.isPinned = entity.isPinned();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    /* =========================
       등록/수정용
    ========================= */
    public NoticeVO(
            String title,
            String content,
            boolean isActive,
            boolean isPinned
    ) {
        this.noticeId = null;
        this.title = title;
        this.content = content;
        this.writerId = null;
        this.isActive = isActive;
        this.isPinned = isPinned;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
