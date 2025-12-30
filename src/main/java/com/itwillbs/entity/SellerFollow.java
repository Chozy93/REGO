package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.SellerFollowVO;

@Entity
@Table(
    name = "seller_follow",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_seller_follow_seller_follower",
            columnNames = {"seller_id", "follower_id"}
        )
    }
)
@Getter
public class SellerFollow {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    /* =========================
       판매자 (팔로우 대상)
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "seller_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_seller_follow_seller")
    )
    private SellerProfile seller;

    /* =========================
       팔로워 (사용자)
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "follower_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_seller_follow_follower")
    )
    private User follower;

    /* =========================
       생성일
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected SellerFollow() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public SellerFollow(SellerProfile seller, User follower, SellerFollowVO vo) {
        this.seller = seller;
        this.follower = follower;
        this.createdAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public SellerFollowVO toVO() {
        return new SellerFollowVO(this);
    }
}
