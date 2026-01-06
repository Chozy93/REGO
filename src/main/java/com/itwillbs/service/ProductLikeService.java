package com.itwillbs.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ProductLikeService {
	
	private final Map<Long, Boolean> likedMap = new HashMap<>();
	private final Map<Long, Integer> likeCountMap = new HashMap<>();
	
	// =========================
    // 찜 토글 로직 (더미)
    // =========================
	
	public Map<String, Object> toggleLike(Long productId, String usernaId) {
		
		boolean liked = likedMap.getOrDefault(productId, false);
		int likeCount = likeCountMap.getOrDefault(productId, 12);
		
		liked = !liked;
		
		if (liked) likeCount++;
		else  likeCount--;
		
		likedMap.put(productId, liked);
		likeCountMap.put(productId, likeCount);
		
		
		Map<String, Object> result = new HashMap<>();
		result.put("liked", liked);
		result.put("likeCount", likeCount);
		
		return result;
	}
	
	// ⭐ 조회용 (메인/상세 공통)
	public boolean isLiked(Long productId, String userId) {
		return likedMap.getOrDefault(productId, false);
	}
	
	public int getLikeCount(Long productId) {
		return likeCountMap.getOrDefault(productId, 12);
	}

}
