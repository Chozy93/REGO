package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.UserFavoriteVO;

@Entity
@Table(
    name = "user_favorites",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_favorites_user_product",
            columnNames = {"user_id", "product_id"}
        )
    }
)
@Getter
public class UserFavorite {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long favoriteId;

    /* =========================
       사용자 (N:1)
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_user_favorites_user")
    )
    private User user;

    /* =========================
       상품 (N:1)
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_user_favorites_product")
    )
    private Product product;

    /* =========================
       생성일
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected UserFavorite() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public UserFavorite(User user, Product product, UserFavoriteVO vo) {
        this.user = user;
        this.product = product;
        this.createdAt = LocalDateTime.now();
    }

    /* =========================
       Entity → VO
    ========================= */
    public UserFavoriteVO toVO() {
        return new UserFavoriteVO(this);
    }
}
