package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

import com.itwillbs.domain.RegionVO;

@Entity
@Table(
    name = "region",
    indexes = {
        @Index(name = "idx_region_parent", columnList = "parent_code"),
        @Index(name = "idx_region_level", columnList = "region_level"),
        @Index(name = "idx_region_parent_active", columnList = "parent_code, is_active")
    }
)
@Getter
public class Region {

    /* =========================
       PK (행안부 법정동 코드)
    ========================= */
    @Id
    @Column(name = "region_code", length = 20)
    private String regionCode;

    /* =========================
       지역명
    ========================= */
    @Column(name = "region_name", length = 100, nullable = false)
    private String regionName;

    /* =========================
       상위 행정구역 (자기참조)
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "parent_code",
        foreignKey = @ForeignKey(name = "fk_region_parent")
    )
    private Region parent;

    /* =========================
       지역 단계 (1 / 2 / 3)
    ========================= */
    @Column(name = "region_level", nullable = false)
    private int regionLevel;

    /* =========================
       지역 타입
       (SIDO, SIGUNGU, EUP, MYEON, DONG, RI)
    ========================= */
    @Column(name = "region_type", length = 20, nullable = false)
    private String regionType;

    /* =========================
       사용 여부
    ========================= */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /* =========================
       날짜
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /* =========================
       JPA 전용 기본 생성자
    ========================= */
    protected Region() {}

    /* =========================
       생성자 (VO → Entity)
    ========================= */
    public Region(Region parent, RegionVO vo) {
        this.regionCode = vo.getRegionCode();
        this.regionName = vo.getRegionName();
        this.parent = parent;
        this.regionLevel = vo.getRegionLevel();
        this.regionType = vo.getRegionType();
        this.isActive = vo.isActive();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    /* =========================
    행정구역 정보 동기화 (UPDATE 전용)
    - 이름 변경
    - 타입 변경
    - 사용 여부 변경
 ========================= */
 public void updateFromVO(RegionVO vo) {

     boolean changed = false;

     if (!Objects.equals(this.regionName, vo.getRegionName())) {
         this.regionName = vo.getRegionName();
         changed = true;
     }

     if (!Objects.equals(this.regionType, vo.getRegionType())) {
         this.regionType = vo.getRegionType();
         changed = true;
     }

     if (this.isActive != vo.isActive()) {
         this.isActive = vo.isActive();
         changed = true;
     }

     if (changed) {
         this.updatedAt = LocalDateTime.now();
     }
 }

    /* =========================
       Entity → VO
    ========================= */
    public RegionVO toVO() {
        return new RegionVO(this);
    }
}
