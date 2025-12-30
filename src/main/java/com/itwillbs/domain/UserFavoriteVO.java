package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.UserFavorite;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserFavoriteVO {

    private final Long favoriteId;
    private final Long userId;
    private final Long productId;
    private final LocalDateTime createdAt;

    /* =========================
       Entity → VO 생성자
    ========================= */
    public UserFavoriteVO(UserFavorite entity) {
        this.favoriteId = entity.getFavoriteId();
        this.userId = entity.getUser().getUserId();
        this.productId = entity.getProduct().getProductId();
        this.createdAt = entity.getCreatedAt();
    }
}
