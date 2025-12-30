package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.UserActivityRegionVO;

@Entity
@Table(name = "user_activity_region")
@Getter
public class UserActivityRegion {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId;

    /* =========================
       사용자 (N:1)
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_user_activity_region_user")
    )
    private User user;

    /* =========================
       지역 정보
    ========================= */
    @Column(name = "region_code", length = 20, nullable = false)
    private String regionCode;

    @Column(name = "priority", nullable = false)
    private int priority;

    /* =========================
       생성일
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected UserActivityRegion() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public UserActivityRegion(User user, UserActivityRegionVO vo) {
        this.user = user;
        this.regionCode = vo.getRegionCode();
        this.priority = vo.getPriority();
        this.createdAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public UserActivityRegionVO toVO() {
        return new UserActivityRegionVO(this);
    }
}
