package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.UserActivityRegion;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserActivityRegionVO {

    /* =========================
       PK
    ========================= */
    private final Long regionId;

    /* =========================
       사용자 FK
    ========================= */
    private final Long userId;

    /* =========================
       지역 정보
    ========================= */
    private final String regionCode;
    private final int priority;

    /* =========================
       생성일
    ========================= */
    private final LocalDateTime createdAt;

    /* =========================
       Entity → VO 생성자 (유일)
    ========================= */
    public UserActivityRegionVO(UserActivityRegion entity) {
        this.regionId = entity.getRegionId();
        this.userId = entity.getUser().getUserId();
        this.regionCode = entity.getRegionCode();
        this.priority = entity.getPriority();
        this.createdAt = entity.getCreatedAt();
    }
}
