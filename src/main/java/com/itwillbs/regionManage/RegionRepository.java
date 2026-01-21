package com.itwillbs.regionManage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Region;

public interface RegionRepository extends JpaRepository<Region, String> {

    /* =========================
       상위 코드 기준 하위 지역 조회
    ========================= */
    List<Region> findByParent_RegionCodeAndIsActiveTrue(String parentCode);

    /* =========================
       지역 단계별 조회
    ========================= */
    List<Region> findByRegionLevelAndIsActiveTrue(int regionLevel);

    /* =========================
       상위 + 단계 조회
    ========================= */
    List<Region> findByParent_RegionCodeAndRegionLevelAndIsActiveTrue(
            String parentCode,
            int regionLevel
    );
}
