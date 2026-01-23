package com.itwillbs.dto;

import com.itwillbs.entity.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegionDTO {
    private String regionCode;
    private String regionName;
    private String parentName; // 부모 지역명 (예: 부산광역시)
    private int regionLevel;
    private String fullName;

    // Entity를 DTO로 변환하는 생성자
    public RegionDTO(Region entity) {
        this.regionCode = entity.getRegionCode();
        this.regionName = entity.getRegionName();
        this.regionLevel = entity.getRegionLevel();
        
        // 상위 지역 이름을 모두 합치는 로직
        StringBuilder sb = new StringBuilder();
        buildFullName(entity, sb);
        this.fullName = sb.toString().trim();
        
        // Lazy Loading 문제 해결: 필요한 데이터만 미리 뽑아서 저장
        if (entity.getParent() != null) {
            this.parentName = entity.getParent().getRegionName();
        }
        
  
    }
    
    private void buildFullName(Region entity, StringBuilder sb) {
        // 1. 먼저 부모가 있다면 부모부터 이름을 가져오게 함 (재귀)
        if (entity.getParent() != null) {
            buildFullName(entity.getParent(), sb);
        }
        
        // 2. 현재 내 레벨을 확인
        // 레벨 1(예: 부산광역시) 이거나 레벨 2(예: 부산진구)일 때만 이름을 추가
        if (entity.getRegionLevel() == 1 || entity.getRegionLevel() == 2) {
            sb.append(entity.getRegionName()).append(" ");
        }
    }
}