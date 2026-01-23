package com.itwillbs.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.entity.Region;
import com.itwillbs.regionManage.RegionRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionRestController {
	private final RegionRepository regionRepository;

	@GetMapping("/search")
	public ResponseEntity<List<Region>> searchRegions(@RequestParam("keyword") String keyword) {
	    if (keyword == null || keyword.trim().isEmpty()) {
	        return ResponseEntity.ok(List.of()); // 검색어 없으면 빈 리스트 반환
	    }
	 // 1. 공백을 제거한 검색 시도 (데이터에 공백이 없을 경우 대비)
	    String cleanKeyword = keyword.replaceAll("\\s+", "%"); 
	    
	    // 2. 조합 검색 실행
	    List<Region> results = regionRepository.findByCombinedRegionName(keyword.trim());
	    return ResponseEntity.ok(results);
	}
}
