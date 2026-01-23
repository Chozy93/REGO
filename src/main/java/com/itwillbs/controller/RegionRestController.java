package com.itwillbs.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.dto.RegionDTO;
import com.itwillbs.entity.Region;
import com.itwillbs.regionManage.RegionRepository;
import com.itwillbs.service.RegionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionRestController {
	private final RegionRepository regionRepository;
	private final RegionService regionService;

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
	
	@PostMapping("/select")
	@ResponseBody
	public ResponseEntity<?> selectRegion(@RequestParam("regionCode") String regionCode, HttpSession session) {
	    // 1. DB에서 정확한 지역 정보 조회
	    Region region = regionService.getRegionByCode(regionCode);
	 // 🚩 서버 콘솔에 값이 제대로 찍히는지 확인!
       System.out.println(region);
	    if (region != null) {
	        // 2. 세션에 저장 (나중에 다른 페이지에서도 사용 가능)
	    	RegionDTO dto = new RegionDTO(region);
	    	// 🚩 서버 콘솔에 값이 제대로 찍히는지 확인!
	        System.out.println("DTO 이름 확인: " + dto.getRegionName()); 
	        System.out.println("DTO 부모 확인: " + dto.getParentName());
	    	
	        session.setAttribute("selectedRegion", dto);
	        return ResponseEntity.ok(dto);
	    }
	    
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 지역입니다.");
	}
}
