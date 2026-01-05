package com.itwillbs.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ProductLikeService {
	
	// ⚠️ 더미 상태 (DB 없음)
	private boolean liked = false;
	private int likeCount = 12;
	
	// =========================
    // 찜 토글 로직 (더미)
    // =========================
	
	public Map<String, Object> toggleLike(Long productId, String username) {
		
		liked = !liked;
		
		if (liked) {
			likeCount++;
		} else {
			likeCount--;
		}
		
		Map<String, Object> result = new HashMap<>();
		result.put("liked", liked);
		result.put("likeCount", likeCount);
		
		return result;
	}

}
