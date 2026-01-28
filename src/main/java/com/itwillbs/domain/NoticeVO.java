package com.itwillbs.domain;

import java.time.LocalDateTime;


import com.itwillbs.entity.Notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter // 폼 바인딩을 위해 Setter 추가
@ToString
@NoArgsConstructor // 1. 스프링이 객체를 생성할 수 있도록 기본 생성자 추가
@AllArgsConstructor // 2. 모든 필드를 인자로 받는 생성자 추가
public class NoticeVO {

    private Long noticeId;
    private String title;
    private String content;
    private String category;  // 추가: 배지 분류용
    private int viewCount;    // 추가: 조회수 표시용
    private Long writerId;
    private boolean active;
    private boolean pinned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* =========================
       Entity → VO (조회)
    ========================= */
    public NoticeVO(Notice entity) {
        this.noticeId = entity.getNoticeId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.category = entity.getCategory(); // 추가
        this.viewCount = entity.getViewCount(); // 추가
        this.writerId = entity.getWriterId();
        this.active = entity.isActive();
        this.pinned = entity.isPinned();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    /* =========================
       등록/수정용
    ========================= */
    public NoticeVO(
            String title,
            String content,
            String category,
            boolean isActive,
            boolean isPinned
    ) {
        this.noticeId = null;
        this.title = title;
        this.content = content;
        this.category = category; // 추가
        this.viewCount = 0;       // 등록 시 초기값 0
        this.writerId = null;
        this.active = isActive;
        this.pinned = isPinned;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
