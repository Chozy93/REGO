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
    private final int regionLevel;     // 1 / 2 / 3
    private final String regionType;   // SIDO / SIGUNGU / ...
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
        this.regionType = entity.getRegionType();
        this.isActive = entity.isActive();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    /* =========================
    Service → VO 생성자
 ========================= */
 public RegionVO(
         String regionCode,
         String regionName,
         String parentCode,
         int regionLevel,
         String regionType,
         boolean isActive
 ) {
     this.regionCode = regionCode;
     this.regionName = regionName;
     this.parentCode = parentCode;
     this.regionLevel = regionLevel;
     this.regionType = regionType;
     this.isActive = isActive;
     this.createdAt = null;
     this.updatedAt = null;
 }
}
