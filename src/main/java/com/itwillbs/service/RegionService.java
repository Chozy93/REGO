package com.itwillbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.entity.Region;
import com.itwillbs.regionManage.RegionRepository;

@Service
public class RegionService {
	@Autowired
    private RegionRepository regionRepository; // 또는 RegionMapper

	public Region getRegionByCode(String code) {
        // DB에서 조회 후, 없으면 null 반환 (또는 예외 발생)
        return regionRepository.findByRegionCodeAndIsActive(code, true)
                               .orElse(null);
    }
}
