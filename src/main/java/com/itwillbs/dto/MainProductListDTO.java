package com.itwillbs.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public class MainProductListDTO {

    private final Long productId;          // 상품 ID
    private final String title;             // 상품명
    private final int price;                // 가격
    private final String thumbnailUrl;      // 썸네일 이미지
    private final String regionName;         // 지역명
    private final String createdTime;        // 등록 시간 (가공된 문자열)
    private final int likeCount;             // ❤️ 찜 개수
    private final boolean liked; 
    
    public String getTimeAgo() {
        return calculateTimeAgo(this.createdTime);
    }

    public MainProductListDTO(
            Long productId,
            String title,
            int price,
            String thumbnailUrl,
            String regionName,
            String createdTime,
            int likeCount,
            boolean liked
    ) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.regionName = regionName;
        this.createdTime = calculateTimeAgo(createdTime);
        this.likeCount = likeCount;
        this.liked = liked;
    }
    
 // 🔥 시간 계산 유틸리티 메서드
    private String calculateTimeAgo(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return "";
        
        try {
            String cleanedDate = dateStr.replace("T", " ");
            if (cleanedDate.contains(".")) {
                cleanedDate = cleanedDate.substring(0, cleanedDate.indexOf("."));
            }

            // 초가 있든 없든 다 잡아내는 포맷터
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss][yyyy-MM-dd HH:mm]");
            LocalDateTime createdTime = LocalDateTime.parse(cleanedDate, formatter);
            LocalDateTime now = LocalDateTime.now();
            
            Duration duration = Duration.between(createdTime, now);
            long seconds = Math.abs(duration.getSeconds()); // 음수 방지 (절대값)

            if (seconds < 60) return "방금 전";
            if (seconds < 3600) return (seconds / 60) + "분 전";
            if (seconds < 86400) return (seconds / 3600) + "시간 전";
            if (seconds < 604800) return (seconds / 86400) + "일 전";
            if (seconds < 2592000) return (seconds / 604800) + "주 전"; // 주 단위 추가
            
            return createdTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return dateStr;
        }
    }
    
}


