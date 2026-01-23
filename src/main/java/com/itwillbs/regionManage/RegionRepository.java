package com.itwillbs.regionManage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    /* =========================
    지역명 실시간 검색 (활성화된 지역만)
    ========================= */
    List<Region> findByRegionNameContainingAndIsActiveTrue(String keyword);
    
 // 부모(SIDO) 이름과 본인(SIGUNGU) 이름을 조합해서 검색
    @Query("SELECT r FROM Region r LEFT JOIN r.parent p " +
           "WHERE (CONCAT(COALESCE(p.regionName, ''), ' ', r.regionName) LIKE %:keyword% " +
           "OR r.regionName LIKE %:keyword%) " +
           "AND r.isActive = true")
    List<Region> findByCombinedRegionName(@Param("keyword") String keyword);
}
