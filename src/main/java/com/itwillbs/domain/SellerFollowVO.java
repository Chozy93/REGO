package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.SellerFollow;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SellerFollowVO {

    private final Long followId;
    private final Long sellerId;
    private final Long followerId;
    private final LocalDateTime createdAt;

    /* =========================
       Entity → VO 생성자
    ========================= */
    public SellerFollowVO(SellerFollow entity) {
        this.followId = entity.getFollowId();
        this.sellerId = entity.getSeller().getSellerId();
        this.followerId = entity.getFollower().getUserId();
        this.createdAt = entity.getCreatedAt();
    }
}
