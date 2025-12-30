package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.Region;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RegionVO {

    private final String regionCode;
    private final String regionName;
    private final String parentCode;
    private final String regionLevel;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /* =========================
       Entity → VO 생성자
    ========================= */
    public RegionVO(Region entity) {
        this.regionCode = entity.getRegionCode();
        this.regionName = entity.getRegionName();
        this.parentCode = entity.getParent() != null
                ? entity.getParent().getRegionCode()
                : null;
        this.regionLevel = entity.getRegionLevel();
        this.isActive = entity.isActive();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
